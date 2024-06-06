package com.mambastu.core.engine;

import java.util.LinkedList;

import com.mambastu.core.logic.LogicManager;
import com.mambastu.infuse.pojo.config.GlobalConfig;
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
    private AnimationTimer timer;
    private LogicManager logicManager;
    private Long lastUpdateTime = System.nanoTime();
    private Boolean isPause = false;
    

    @Getter
    @Setter
    public static class EngineProps { // 单例模式静态内部类 引擎参数
        private static final EngineProps INSTANCE = new EngineProps();
        
        private GlobalConfig config;
        
        private Scene scene;
        private Pane root = new Pane();
        private AnimationTimer timer;
        private Long lastUpdateTime;
        private Boolean isPause;

        private BasePlayer player;
        private LinkedList<BaseMonster> monsterList = new LinkedList<>();
        private LinkedList<BaseBullet> bulletList = new LinkedList<>();
        private LinkedList<BaseBarrier> barrierList = new LinkedList<>();

        private EngineProps() {} // 私有构造方法 防止在外部被实例化

        public static EngineProps getInstance() {
            return EngineProps.INSTANCE;
        }
    }

    public GameEngine(GlobalConfig config, Scene scene) {
        scene.setRoot(EngineProps.getInstance().getRoot()); // 切换显示节点
        initEngineProps(config, scene);
        this.logicManager = new LogicManager(EngineProps.getInstance()); // 传递引擎参数初始化逻辑管理器
        System.out.println("Game Engine successfully initialized!");
    }

    private void initEngineProps(GlobalConfig config, Scene scene) {
        EngineProps.getInstance().setConfig(config);
        EngineProps.getInstance().setScene(scene);
        EngineProps.getInstance().setIsPause(isPause);
        EngineProps.getInstance().setTimer(timer);
        EngineProps.getInstance().setLastUpdateTime(lastUpdateTime);
    }

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
        System.out.println("Game Engine start");
    }


}
