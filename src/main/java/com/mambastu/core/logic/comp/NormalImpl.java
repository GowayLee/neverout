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
import com.mambastu.material.pojo.entity.enums.CollisionState;
import com.mambastu.material.pojo.entity.barrier.BaseBarrier;
import com.mambastu.material.pojo.entity.bullet.BaseBullet;
import com.mambastu.material.pojo.entity.monster.BaseMonster;
import com.mambastu.material.pojo.entity.monster.MonsterTypes;
import com.mambastu.material.pojo.entity.player.BasePlayer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class NormalImpl implements ModeLogic { // TODO: 加入RecordManager以及相关组件, 规划oop设计
    private final LogicLayerListener listener;
    private final InputHandler inputListener;

    private final EventManager eventManager;

    private final Context ctx;
    private final Pane gamePane;

    private boolean isPause;
    private ArrayList<Timeline> monsterEggTimerList;

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

    public void initPlayer() {
        try {
            player.setPos(gamePane.getWidth(), gamePane.getHeight());
            player.putOnPane(gamePane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initMonsterGenTimer() {
        monsterEggTimerList = new ArrayList<>();
        for (Map.Entry<MonsterTypes, Double> eggEntry : ctx.getLevelConfig().getMonsterEggList()
                .entrySet()) {
            Timeline monsterEggTimer = new Timeline();
            monsterEggTimer.getKeyFrames()
                    .add(new KeyFrame(
                            Duration.millis((long) (ctx.getLevelConfig().getMonsterScalDensity() * eggEntry.getValue())),
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
                    BaseMonster monster = MonsterFactory.getMonsterFactory().create(eggType); // TODO: 对象池实现
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

    public void updateEntity(long elapsedTime) { // 游戏循环更新
        checkCollision();
        playerMove();
        monsterMove();
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
        for (BaseMonster monster : monsterList) { // HACK: 替换改进碰撞检测逻辑
            if (player.getBounds().collisionState(monster.getBounds()) == CollisionState.TRUE) { // 触发事件
                CollisionEvent event = new CollisionEvent(player, monster);
                eventManager.fireEvent(event);
                if (player.isDie()) { // 检查玩家是否死亡
                    PlayerDieEvent playerDieEvent = new PlayerDieEvent(player, monsterList, gamePane);
                    eventManager.fireEvent(playerDieEvent);
                    stopEngine();
                }
            }
        }
    }

    // ================================= EngineState Control Section =================================
    
    private void pauseEngine() { // 游戏暂停时调用，暂停引擎
        isPause = true;
        listener.pauseEngine();
        stopMonsterGenTimer();
    }

    private void resumeEngine() { // 游戏恢复时调用，恢复引擎
        isPause = false;
        listener.resumeEngine();
        startMonsterGenTimer();
    }

    private void stopEngine() { // 游戏结束时调用，关闭引擎并且清空实体们
        listener.stopEngine();
        stopMonsterGenTimer();
        clearAllEntity();
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
        monsterList.clear();
        monsterEggTimerList.clear();
        bulletList.clear();
        barrierList.clear();
    }

}
