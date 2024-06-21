package com.mambastu.core;

import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mambastu.controller.context.dto.Context;
import com.mambastu.controller.listener.EngineLayerListener;
import com.mambastu.controller.listener.LogicLayerListener;
import com.mambastu.gameobjects.bound.ScreenBound;
import com.mambastu.gameobjects.entity.barrier.BaseBarrier;
import com.mambastu.gameobjects.entity.bullet.BaseBullet;
import com.mambastu.gameobjects.entity.monster.BaseMonster;
import com.mambastu.gameobjects.entity.player.BasePlayer;
import com.mambastu.utils.GlobalVar;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import lombok.Getter;
import lombok.Setter;

public class GameEngine {
    private static final Logger logger = LogManager.getLogger(GameEngine.class);

    private final EngineLayerListener listener;

    private final LogicLayerHandler logicLayerHandler; // 初始化逻辑层处理器 用于处理逻辑层事件 例如暂停游戏，继续游戏等事件

    private final LogicManager logicManager;

    private final StackPane root;
    private final GameRegistry gameRegistry;
    private AnimationTimer timer;

    /**
     * Contain all the parameters of Engine Layer and also Logic Layer inside the Layer
     * 
     */
    @Getter
    @Setter
    public class GameRegistry { // 成员内部类 引擎属性将贯穿引擎层与逻辑层
        private final Context ctx;

        private final Pane gamePane;

        private final BasePlayer player;
        private final LinkedList<BaseMonster> monsterList;
        private final LinkedList<BaseBullet> bulletList;
        private final LinkedList<BaseBarrier> barrierList;

        public GameRegistry(Context ctx) {
            this.ctx = ctx; // 引擎配置参数
            this.gamePane = new Pane();
            ScreenBound.init(gamePane); // 初始化游戏画布边界
            this.player = ctx.getLevelConfig().getPlayer();
            this.monsterList = new LinkedList<>();
            this.bulletList = new LinkedList<>();
            this.barrierList = new LinkedList<>();
        }
    }

    // ================================= Init Section =================================

    public GameEngine(Context ctx, StackPane root, EngineLayerListener listener) {
        this.listener = listener;
        this.logicLayerHandler = new LogicLayerHandler();
        this.root = root;
        this.gameRegistry = new GameRegistry(ctx);
        this.logicManager = LogicManager.getInstance();
        this.logicManager.init(gameRegistry, logicLayerHandler); // 传递引擎参数初始化逻辑管理器
        logger.info("Game Engine successfully initialized!");
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
        public void stopEngine(boolean isPassLevel) {
            timer.stop();
            // TODO: 回收引擎中的资源
            listener.stopGame(isPassLevel);
        }
    }

    // ================================= Control Section =================================
    public void showGamePane() { // 显示游戏画布
        Pane gamePane = gameRegistry.getGamePane();
        GlobalVar.setGamePane(gamePane);
        root.getChildren().remove(gamePane);
        root.getChildren().add(gamePane); // 将游戏画布节点压入StackPane
    }

    public void hideGamePane() { // 隐藏游戏画布
        root.getChildren().remove(gameRegistry.getGamePane());
    }

    public void pauseThisEngine() { // 暂停引擎
        timer.stop();
    }

    public void resumeThisEngine() { // 恢复引擎
        timer.start();
    }
    // ================================= Start Section =================================

    public void start() {
        logicManager.initEntities(); // 初始化怪物蛋
        logicManager.initTimers(); // 初始化玩家以及怪物蛋定时孵化器
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                logicManager.update();
            }
        };
        timer.start();
        logger.info("Game Engine start");
    }

}
