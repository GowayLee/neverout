package com.mambastu.core.engine;

import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mambastu.controller.level.comp.config.GlobalConfig;
import com.mambastu.core.logic.LogicManager;
import com.mambastu.listener.EngineLayerListener;
import com.mambastu.listener.LogicLayerListener;
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
    private static final Logger logger = LogManager.getLogger(GameEngine.class);

    private final EngineLayerListener listener;

    private final LogicManager logicManager;

    private final LogicLayerHandler logicLayerHandler; // 初始化逻辑层处理器 用于处理逻辑层事件 例如暂停游戏，继续游戏等事件

    private AnimationTimer timer;
    private Long lastUpdateTime = System.nanoTime();

    @Getter
    @Setter
    public static class EngineProps { // 单例模式静态内部类 引擎属性将贯穿引擎层与逻辑层 TODO: 考虑是否需要使用代理模式进行封装
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

    public GameEngine(GlobalConfig config, Scene scene, EngineLayerListener listener) {
        this.listener = listener;
        this.logicLayerHandler = new LogicLayerHandler();
        scene.setRoot(EngineProps.getInstance().getRoot()); // 切换显示节点
        initEngineProps(config, scene);
        this.logicManager = new LogicManager(EngineProps.getInstance(), logicLayerHandler); // 传递引擎参数初始化逻辑管理器
        logger.info("Game Engine successfully initialized!");
    }

    private void initEngineProps(GlobalConfig config, Scene scene) {
        EngineProps.getInstance().setConfig(config);
        EngineProps.getInstance().setScene(scene);
    }

    private class LogicLayerHandler implements LogicLayerListener {
        @Override
        public void pauseEngine() {
            timer.stop();
            listener.pauseGame();
        }

        @Override
        public void resumeEngine() {
            timer.start();
            listener.resumeGame();
        }

        @Override
        public void stopEngine() {
            listener.stopGame();
        }
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
        timer.start();
        logger.info("Game Engine start");
    }

}
