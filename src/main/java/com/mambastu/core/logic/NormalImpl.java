package com.mambastu.core.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.mambastu.controller.input.InputManager;
import com.mambastu.controller.level.context.dto.Context;
import com.mambastu.controller.level.context.dto.config.LevelConfig.MonsterEgg;
import com.mambastu.core.EventManager;
import com.mambastu.core.GameEngine.EngineProps;
import com.mambastu.core.event.BulletHitMonsterEvent;
import com.mambastu.core.event.MonsterDieEvent;
import com.mambastu.core.event.MonsterHitPlayerEvent;
import com.mambastu.core.event.PlayerDieEvent;
import com.mambastu.factories.BulletFactory;
import com.mambastu.factories.MonsterFactory;
import com.mambastu.listener.InputListener;
import com.mambastu.listener.LogicLayerListener;
import com.mambastu.material.pojo.entity.barrier.BaseBarrier;
import com.mambastu.material.pojo.entity.bullet.BaseBullet;
import com.mambastu.material.pojo.enums.CollisionState;

import com.mambastu.material.pojo.entity.monster.BaseMonster;
import com.mambastu.material.pojo.entity.monster.MonsterTypes;
import com.mambastu.material.pojo.entity.player.BasePlayer;
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

    private final Context ctx;
    private final Pane gamePane;

    private boolean isPause;
    private final Timeline countDownTimer;
    private final ArrayList<Timeline> monsterEggTimerList;

    private BasePlayer player;
    private final LinkedList<BaseMonster> monsterList;
    private final LinkedList<BaseBullet> bulletList;
    private final LinkedList<BaseBarrier> barrierList;

    private final List<BaseBullet> removeList = new ArrayList<>();
    private final Set<BaseMonster> hittedSet = new HashSet<>();

    // ================================= Init Section
    // =================================

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

    @Override
    public void initCountDownTimer() { // 初始化倒计时，并将其添加到游戏画布中。
        ctx.getLevelRecord().getRemainDuration().set(ctx.getLevelConfig().getDuration());
        countDownTimer.getKeyFrames().add(new KeyFrame(
                Duration.seconds(1), event -> {
                    ctx.getLevelRecord().getRemainDuration().set(ctx.getLevelRecord().getRemainDuration().get() - 1);
                    checkIsGamePass();
                }));
        countDownTimer.setCycleCount(Timeline.INDEFINITE);
        countDownTimer.play();
    }

    @Override
    public void initPlayer() { // 初始化玩家位置和属性，并将其添加到游戏画布中。
        try {
            player.init();
            player.setPos(gamePane.getWidth(), gamePane.getHeight());
            player.putOnPane(gamePane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initMonsterGenTimer() {
        for (MonsterEgg eggEntry : ctx.getLevelConfig().getMonsterEggList()) {
            Timeline monsterEggTimer = new Timeline();
            monsterEggTimer.getKeyFrames()
                    .add(new KeyFrame(
                            Duration.millis(
                                    (long) (ctx.getLevelConfig().getMonsterScalDensity() * eggEntry.getSpawnTime())),
                            generateMonster(eggEntry.getMonsterType())));
            monsterEggTimer.setCycleCount(eggEntry.getSpawnCount());
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
                    monster.omen(monsterList); // 生成预警
                    monster.setPos(gamePane.getWidth(), gamePane.getHeight(), player);
                    monster.putOnPane(gamePane);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    // ================================= Update Logic Section
    // =================================

    @Override
    public void update() { // 游戏循环更新
        checkMonsterHitPlayer();
        checkBullletHitMonster();
        playerMove();
        monsterMove();
        bulletMove();
        playerFire();
    }

    private void monsterMove() {
        for (BaseMonster monster : monsterList) {
            monster.move(player.getX().get(), player.getY().get());
        }
    }

    private void playerMove() {
        player.move(InputManager.getInstance().getActiveInputs());
    }

    private void bulletMove() {
        for (BaseBullet bullet : bulletList) {
            bullet.move(); // 子弹移动逻辑
        }
    }

    private void playerFire() {
        if (player.getWeapon() != null) {
            List<BaseBullet> newBulletList = player.getWeapon().fire(player.getX().get(), player.getY().get(),
                    monsterList, InputManager.getInstance().getActiveInputs());
            if (newBulletList != null && newBulletList.size() > 0) {
                bulletList.addAll(newBulletList); // 添加新子弹到子弹列表中
            }
        }
    }

    private void checkMonsterHitPlayer() {
        for (BaseMonster monster : monsterList) {
            if (player.getBound().collisionState(monster.getBound()) == CollisionState.TRUE) { // 触发事件
                MonsterHitPlayerEvent event = MonsterHitPlayerEvent.getInstance();
                event.setProperty(player, monster);
                EventManager.getInstance().fireEvent(event);
                checkIsGameFail();
            }
        }
    }

    private void checkBullletHitMonster() { // 检查子弹是否击中怪物，触发事件等操作
        removeList.clear(); // 记录需要移除的子弹列表，因为不能在循环中直接移除元素，会导致并发修改异常
        hittedSet.clear(); // 记录被击中的怪物集合，使用集合防止一个怪物被多次添加多次死亡造成对象池异常
        for (BaseBullet bullet : bulletList) {
            for (BaseMonster monster : monsterList) {
                if (bullet.getBound().collisionState(monster.getBound()) == CollisionState.TRUE) {
                    BulletHitMonsterEvent event = BulletHitMonsterEvent.getInstance(); // 触发子弹击中怪物事件，记录数据等操作
                    event.setProperty(bullet, monster);
                    EventManager.getInstance().fireEvent(event);
                    hittedSet.add(monster); // 记录被击中的怪物，后续统一处理
                }
                if (!bullet.isValid()) { // 如果子弹无效，则需要移除该子弹
                    removeList.add(bullet);
                    break;
                }
            }
        }
        for (BaseMonster monster : hittedSet) {
            checkMonsterDie(monster);
        }
        for (BaseBullet removBullet : removeList) {
            bulletList.remove(removBullet); // 移除子弹
            removBullet.removeFromPane(gamePane);
            BulletFactory.getInstance().delete(removBullet);
        }
    }

    private void checkMonsterDie(BaseMonster monster) {
        if (monster.isDie()) {
            MonsterDieEvent event = MonsterDieEvent.getInstance(); // 因为怪物有死亡延迟效果，为了保证对象池中对象的可用性
            event.setProperty(monster);
            EventManager.getInstance().fireEvent(event); // 在怪物死亡效果结束后再放回对象池
            monsterList.remove(monster); // 移除怪物
            ctx.getLevelRecord().getKillCount().set(ctx.getLevelRecord().getKillCount().get() + 1);
        }
    }

    private void checkIsGamePass() { // 检查游戏是否失败，例如玩家死亡等条件
        if (ctx.getLevelRecord().getRemainDuration().get() <= 0) {
            stopEngine(true);
        }
    }

    private void checkIsGameFail() { // 检查游戏是否结束，例如玩家死亡等条件
        if (player.isDie()) {
            PlayerDieEvent event = PlayerDieEvent.getInstance(); // 触发玩家死亡事件，记录数据等操作
            event.setProperty(player);
            EventManager.getInstance().fireEvent(event);
            stopEngine(false);
        }
    }
    // ================================= EngineState Control Section
    // =================================

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

    private void stopEngine(boolean isPassLevel) { // 游戏结束时调用，关闭引擎并且清空实体们
        stopMonsterGenTimer();
        stopCountDownTimer();
        clearAllEntity();
        ObjectPoolManager.getInstance().close(); // 关闭对象池，释放内存
        listener.stopEngine(isPassLevel);
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
            monster.die(); // 怪物死亡，触发死亡效果，然后移除怪物
        }
        monsterList.clear();
        monsterEggTimerList.clear();
        bulletList.clear();
        barrierList.clear();
    }

}
