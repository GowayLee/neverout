package com.mambastu.core.logic.comp;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import com.mambastu.core.engine.GameEngine.EngineProps;
import com.mambastu.core.event.EventManager;
import com.mambastu.core.event.comp.event.CollisionEvent;
import com.mambastu.core.event.comp.event.PlayerDieEvent;
import com.mambastu.infuse.input.InputManager;
import com.mambastu.infuse.level.comp.config.GlobalConfig;
import com.mambastu.material.pojo.entity.barrier.BaseBarrier;
import com.mambastu.material.pojo.entity.bullet.BaseBullet;
import com.mambastu.material.pojo.entity.monster.BaseMonster;
import com.mambastu.material.pojo.entity.player.BasePlayer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class NormalImpl implements ModeLogic { // TODO: 加入RecordManager以及相关组件, 规划oop设计
    private final PropertyChangeSupport support;

    private EventManager eventManager;
    private InputManager inputManager;

    private GlobalConfig config;
    private Pane root;

    @SuppressWarnings("unused")
    private boolean isPause = false;
    @SuppressWarnings("unused")
    private boolean isGameOver = false;
    private ArrayList<Timeline> monsterEggTimerList;

    private BasePlayer player;
    private LinkedList<BaseMonster> monsterList;
    private LinkedList<BaseBullet> bulletList;
    private LinkedList<BaseBarrier> barrierList;

    // ================================= Init Section =================================

    public NormalImpl(EngineProps engineProps) {
        this.support = new PropertyChangeSupport(this);
        this.config = engineProps.getConfig();
        this.player = engineProps.getPlayer();
        this.monsterList = engineProps.getMonsterList();
        this.bulletList = engineProps.getBulletList();
        this.barrierList = engineProps.getBarrierList();
        this.root = engineProps.getRoot();
        this.eventManager = new EventManager();
        this.inputManager = new InputManager(engineProps.getScene());

        inputManager.addPropertyListener(event -> { // 设置InputManager暂停侦听器
            if ((boolean) event.getNewValue()) {
                pauseEngine();
            } else {
                resumeEngine();
            }
        });
    }

    public void addPropertyListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void initPlayer() {
        try {
            player = config.getLevelConfig().getPlayerEgg().getDeclaredConstructor().newInstance();
            player.setPos(root.getWidth(), root.getHeight());
            player.putOnPane(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initMonsterGenTimer() {
        monsterEggTimerList = new ArrayList<>();
        for (Map.Entry<Class<? extends BaseMonster>, Double> eggEntry : config.getLevelConfig().getMonsterEggList()
                .entrySet()) {
            Timeline monsterEggTimer = new Timeline();
            monsterEggTimer.getKeyFrames()
                    .add(new KeyFrame(
                            Duration.millis((long) (config.getLevelConfig().getMonsterScalNum() * eggEntry.getValue())),
                            generateMonster(eggEntry.getKey())));
            monsterEggTimer.setCycleCount(Timeline.INDEFINITE);
            monsterEggTimerList.add(monsterEggTimer);
        }
    }

    private <T extends BaseMonster> EventHandler<ActionEvent> generateMonster(Class<T> eggClass) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    T monster = eggClass.getDeclaredConstructor().newInstance(); // TODO: 对象池实现
                    monster.setPos(root.getWidth(), root.getHeight(), player);
                    monster.putOnPane(root);
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
            monster.move(player.getX(), player.getY());
        }
    }

    private void playerMove() {
        player.move(inputManager.getActiveInputs());
    }

    private void checkCollision() {
        for (BaseMonster monster : monsterList) { // HACK: 替换改进碰撞检测逻辑
            double playerCenterX = player.getX() + player.getImageView().getFitWidth() / 2;
            double playerCenterY = player.getY() + player.getImageView().getFitHeight() / 2;
            double monsterCenterX = monster.getImageView().getX() + monster.getImageView().getFitWidth() / 2;
            double monsterCenterY = monster.getImageView().getY() + monster.getImageView().getFitHeight() / 2;

            double distance = Math
                    .sqrt(Math.pow(playerCenterX - monsterCenterX, 2) + Math.pow(playerCenterY - monsterCenterY, 2));
            if (distance < (player.getImageView().getFitWidth() / 2 + monster.getImageView().getFitWidth() / 2)) { // 触发事件
                CollisionEvent event = new CollisionEvent(player, monster);
                eventManager.fireEvent(event);
                if (player.isDie()) { // 检查玩家是否死亡
                    PlayerDieEvent playerDieEvent = new PlayerDieEvent(player, monsterList, root);
                    eventManager.fireEvent(playerDieEvent);
                    stopEngine();
                }
            }
        }
    }

    // ================================= EngineState Control Section =================================
    
    private void pauseEngine() { // 游戏暂停时调用，暂停引擎
        isPause = true;
        support.firePropertyChange("isPause", !isPause, isPause);
        stopMonsterGenTimer();
    }

    private void resumeEngine() { // 游戏恢复时调用，恢复引擎
        isPause = false;
        support.firePropertyChange("isPause", !isPause, isPause);
        startMonsterGenTimer();
    }

    private void stopEngine() { // 游戏结束时调用，关闭引擎并且清空实体们
        isGameOver = true;
        support.firePropertyChange("isGameOver", !isGameOver, isGameOver);
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
