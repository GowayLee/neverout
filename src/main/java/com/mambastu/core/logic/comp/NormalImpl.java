package com.mambastu.core.logic.comp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import com.mambastu.controller.input.InputManager;
import com.mambastu.controller.level.context.dto.Context;
import com.mambastu.core.engine.GameEngine.EngineProps;
import com.mambastu.core.event.EventManager;
import com.mambastu.core.event.comp.event.CollisionEvent;
import com.mambastu.core.event.comp.event.PlayerDieEvent;
import com.mambastu.listener.InputListener;
import com.mambastu.listener.LogicLayerListener;
import com.mambastu.material.factories.MonsterFactory;
import com.mambastu.material.pojo.entity.BaseEntity;
import com.mambastu.material.pojo.entity.barrier.BaseBarrier;
import com.mambastu.material.pojo.entity.bullet.BaseBullet;
import com.mambastu.material.pojo.entity.monster.BaseMonster;
import com.mambastu.material.pojo.entity.monster.MonsterTypes;
import com.mambastu.material.pojo.entity.player.BasePlayer;
import com.mambastu.material.pools.ObjectPool;
import com.mambastu.material.pools.ObjectPoolManager;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class NormalImpl implements ModeLogic {
    private final LogicLayerListener listener;
    private final InputHandler inputListener;

    private final EventManager eventManager;

    private final Context ctx;
    private final Pane gamePane;

    private boolean isPause;
    private final Timeline countDownTimer;
    private final ArrayList<Timeline> monsterEggTimerList;

    private BasePlayer player;
    private final LinkedList<BaseMonster> monsterList;
    private final LinkedList<BaseBullet> bulletList;
    private final LinkedList<BaseBarrier> barrierList;

    // ================================= Init Section =================================

    public NormalImpl(EngineProps engineProps, LogicLayerListener listener) {
        this.listener = listener;
        this.inputListener = new InputHandler();
        this.ctx = engineProps.getCtx();
        this.player = engineProps.getPlayer();
        this.countDownTimer = new Timeline();
        this.monsterEggTimerList = new ArrayList<>();
        this.monsterList = engineProps.getMonsterList();
        this.bulletList = engineProps.getBulletList();
        this.barrierList = engineProps.getBarrierList();
        this.gamePane = engineProps.getGamePane();
        this.eventManager = new EventManager();
        InputManager.getInstance().addListener(inputListener);
    }

    private class InputHandler implements InputListener {
        @Override
        public void switchPausenResume() {
            if (isPause) {
                resumeEngine();
            } else {
                pauseEngine();
            }
        }
    }

    public void initCountDownTimer() { // 初始化倒计时，并将其添加到游戏画布中。
        ctx.getLevelRecord().getRemainDuration().set(ctx.getLevelConfig().getDuration());
        countDownTimer.getKeyFrames().add(new KeyFrame(
            Duration.seconds(1), event -> {
                ctx.getLevelRecord().getRemainDuration().set(ctx.getLevelRecord().getRemainDuration().get() - 1);
            }));
        countDownTimer.setCycleCount(Timeline.INDEFINITE);
        countDownTimer.play();
    }

    public void initPlayer() { // 初始化玩家位置和属性，并将其添加到游戏画布中。
        try {
            player.setPos(gamePane.getWidth(), gamePane.getHeight());
            player.putOnPane(gamePane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initMonsterGenTimer() {
        for (Map.Entry<MonsterTypes, Double> eggEntry : ctx.getLevelConfig().getMonsterEggList()
                .entrySet()) {
            Timeline monsterEggTimer = new Timeline();
            monsterEggTimer.getKeyFrames()
                    .add(new KeyFrame(
                            Duration.millis(
                                    (long) (ctx.getLevelConfig().getMonsterScalDensity() * eggEntry.getValue())),
                            generateMonster(eggEntry.getKey())));
            monsterEggTimer.setCycleCount(Timeline.INDEFINITE);
            monsterEggTimerList.add(monsterEggTimer);
        }
        startMonsterGenTimer();
    }

    private EventHandler<ActionEvent> generateMonster(MonsterTypes eggType) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    BaseMonster monster = MonsterFactory.getInstance().create(eggType);
                    monster.setPos(gamePane.getWidth(), gamePane.getHeight(), player);
                    monster.putOnPane(gamePane);
                    monsterList.add(monster);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    // ================================= Update Logic Section =================================

    public void update(long elapsedTime) { // 游戏循环更新
        checkCollision();
        playerMove();
        monsterMove();
        checkIsGameOver();
    }

    private void monsterMove() {
        for (BaseMonster monster : monsterList) {
            monster.move(player.getX().get(), player.getY().get());
        }
    }

    private void playerMove() {
        player.move(InputManager.getInstance().getActiveInputs());
    }

    private void checkCollision() {
        ArrayList<BaseMonster> delList = new ArrayList<>(); // 测试版
        for (BaseMonster monster : monsterList) { // HACK: 替换改进碰撞检测逻辑
            double playerCenterX = player.getX().get() + player.getShowingImageView().getFitWidth() / 2;
            double playerCenterY = player.getY().get() + player.getShowingImageView().getFitHeight() / 2;
            double monsterCenterX = monster.getShowingImageView().getX() + monster.getShowingImageView().getFitWidth() / 2;
            double monsterCenterY = monster.getShowingImageView().getY() + monster.getShowingImageView().getFitHeight() / 2;

            double distance = Math
                    .sqrt(Math.pow(playerCenterX - monsterCenterX, 2) + Math.pow(playerCenterY - monsterCenterY, 2));
            if (distance < (player.getShowingImageView().getFitWidth() / 2 + monster.getShowingImageView().getFitWidth() / 2)) { // 触发事件
                // CollisionEvent event = new CollisionEvent(player, monster);
                // eventManager.fireEvent(event);
                gamePane.getChildren().remove(monster.getShowingImageView()); // 移除怪物
                delList.add(monster); // 测试版
                MonsterFactory.getInstance().delete(monster);
            }
        }
        for (BaseMonster monster : delList) { // 测试版
            monsterList.remove(monster);
        }
    }

    private void checkIsGameOver() { // 检查游戏是否结束，例如时间结束或者玩家死亡等条件
        if (player.isDie()) {
            PlayerDieEvent event = new PlayerDieEvent(player); // 触发玩家死亡事件，记录数据等操作
            eventManager.fireEvent(event);
            stopEngine();
        }
        if (ctx.getLevelRecord().getRemainDuration().get() <= 0) {
            stopEngine();
        }
    }
    // ================================= EngineState Control Section =================================

    private void pauseEngine() { // 游戏暂停时调用，暂停引擎
        isPause = true;
        listener.pauseEngine();
        stopCountDownTimer();
        stopMonsterGenTimer();
    }

    private void resumeEngine() { // 游戏恢复时调用，恢复引擎
        isPause = false;
        listener.resumeEngine();
        startCountDownTimer();
        startMonsterGenTimer();
    }

    private void stopEngine() { // 游戏结束时调用，关闭引擎并且清空实体们
        stopMonsterGenTimer();
        stopCountDownTimer();
        clearAllEntity();
        listener.stopEngine();
    }

    private void startCountDownTimer() {
        countDownTimer.play();
    }

    private void stopCountDownTimer() {
        countDownTimer.stop();
    }

    private void startMonsterGenTimer() {
        for (Timeline monsterEggTimer : monsterEggTimerList) {
            monsterEggTimer.play();
        }
    }

    private void stopMonsterGenTimer() {
        for (Timeline monsterEggTimer : monsterEggTimerList) {
            monsterEggTimer.stop();
        }
    }

    private void clearAllEntity() {
        for (BaseMonster monster : monsterList) {
            monster.removeFromPane(gamePane);
        }
        monsterList.clear();
        monsterEggTimerList.clear();
        bulletList.clear();
        barrierList.clear();
    }

}
