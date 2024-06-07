package com.mambastu.infuse.level;

import com.mambastu.core.engine.GameEngine;
import com.mambastu.expo.menu.GameOverMenu;
import com.mambastu.expo.menu.LevelMenu;
import com.mambastu.expo.menu.PauseMenu;
import com.mambastu.infuse.level.comp.config.GlobalConfig;
import com.mambastu.infuse.level.comp.config.LevelConfig;

import javafx.scene.Scene;

public class LevelManager {
    private GameEngine gameEngine;
    private GlobalConfig config; // 全局配置信息(需要改一个名称)，包含了游戏的各种参数，穿透关卡管理层、引擎层、逻辑层，可在UI层调用以获取需要显示的信息
    private Scene scene;

    private PauseMenu pauseMenu;
    private LevelMenu levelMenu;
    private GameOverMenu gameOverMenu;

    public LevelManager (Scene scene) {
        this.config = new GlobalConfig();
        this.scene = scene;
    }

    public GlobalConfig getGlobalConfig() {
        return config;
    }

    public LevelConfig getLevelConfig() {
        return config.getLevelConfig();
    }

    public void startLevel() {
        gameEngine = new GameEngine(config, scene);
        initEngineStateListener();
        gameEngine.start();
    }

    private void initEngineStateListener() { // 初始化引擎层引擎运行状态监听器
        gameEngine.addPropertyListener4LevelManager(event -> {
            switch (event.getPropertyName()) {
                case "isPause": // 游戏暂停逻辑
                    if ((boolean) event.getNewValue()) {
                        System.out.println("LevelManager: Game is paused.");
                    } else {
                        System.out.println("LevelManager: Game is resumed.");
                    }
                    break;
                case "isGameOver": // 游戏结束时停止游戏逻辑更新 并触发相应事件
                    System.out.println("LevelManager: Game is over.");
                    break;
                default:
                    break;
            }
        });
    }
}
