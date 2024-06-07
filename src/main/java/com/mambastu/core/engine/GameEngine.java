package com.mambastu.core.engine;

import java.util.LinkedList;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.mambastu.core.logic.LogicManager;
import com.mambastu.infuse.level.comp.config.GlobalConfig;
import com.mambastu.material.pojo.entity.barrier.BaseBarrier;
import com.mambastu.material.pojo.entity.bullet.BaseBullet;
import com.mambastu.material.pojo.entity.monster.BaseMonster;
import com.mambastu.material.pojo.entity.player.BasePlayer;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;

public class GameEngine {
    private final PropertyChangeSupport support;

    private LogicManager logicManager;

    private AnimationTimer timer;
    private Long lastUpdateTime = System.nanoTime();
    private boolean isPause = false;
    private boolean isGameOver = false;

    @Getter
    @Setter
    public static class EngineProps { // 单例模式静态内部类 引擎属性将贯穿引擎层与逻辑层
        private static final EngineProps INSTANCE = new EngineProps();

        private GlobalConfig config;

        private Scene scene;
        private Pane root = new Pane();

        private BasePlayer player;
        private LinkedList<BaseMonster> monsterList = new LinkedList<>();
        private LinkedList<BaseBullet> bulletList = new LinkedList<>();
        private LinkedList<BaseBarrier> barrierList = new LinkedList<>();

        private EngineProps() {
        } // 私有构造方法 防止在外部被实例化

        public static EngineProps getInstance() {
            return EngineProps.INSTANCE;
        }
    }

    // ================================= Init Section =================================

    public GameEngine(GlobalConfig config, Scene scene) {
        this.support = new PropertyChangeSupport(this); // 初始化监听器支持类 用于监听引擎运行状态变化 例如游戏暂停 游戏结束等状态变化时触发相应事件
        scene.setRoot(EngineProps.getInstance().getRoot()); // 切换显示节点
        initEngineProps(config, scene);
        this.logicManager = new LogicManager(EngineProps.getInstance()); // 传递引擎参数初始化逻辑管理器
        System.out.println("Game Engine successfully initialized!");
    }

    private void initEngineProps(GlobalConfig config, Scene scene) {
        EngineProps.getInstance().setConfig(config);
        EngineProps.getInstance().setScene(scene);
    }

    public void addPropertyListener4LevelManager(PropertyChangeListener listener) { // 添加监听器到引擎层，以便在引擎运行状态变化时执行相应的逻辑操作
        support.addPropertyChangeListener(listener);
    }

    private void initEngineStateListener() { // 初始化逻辑层引擎属性监听器(Handler)
        logicManager.addPropertyListener4Engine(event -> {
            switch (event.getPropertyName()) {
                case "isPause": // 游戏暂停逻辑
                    isPause = (boolean) event.getNewValue();
                    support.firePropertyChange("isPause", !isPause, isPause);
                    if (isPause) {
                        timer.stop(); // 暂停游戏时停止游戏逻辑更新
                    } else {
                        timer.start(); // 恢复游戏时重新开始游戏逻辑更新
                    }
                    break;
                case "isGameOver": // 游戏结束时停止游戏逻辑更新 并触发相应事件
                    isGameOver = (boolean) event.getNewValue();
                    support.firePropertyChange("isGameOver", false, isGameOver);
                    break;
                default:
                    break;
            }
        });
    }
    // ================================= Start Section =================================

    public void start() {
        logicManager.initEntity(); // 初始化玩家以及怪物蛋定时孵化器
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastUpdateTime > 0) {
                    long elapsedTime = (now - lastUpdateTime) / 1_000_000; // 计算时间差(ms)
                    logicManager.update(elapsedTime);
                }
                lastUpdateTime = now;
            }
        };
        initEngineStateListener();
        timer.start();
        System.out.println("Game Engine start");
    }

}
