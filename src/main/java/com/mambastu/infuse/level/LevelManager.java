package com.mambastu.infuse.level;

import com.mambastu.core.engine.GameEngine;
import com.mambastu.infuse.level.comp.config.GlobalConfig;
import com.mambastu.infuse.level.comp.config.LevelConfig;

import javafx.scene.Scene;

public class LevelManager {
    private GameEngine gameEngine;
    private GlobalConfig config;
    private Scene scene;

    public LevelManager (Scene scene) {
        this.config = new GlobalConfig();
        this.scene = scene;
    }

    public GlobalConfig getConfig() {
        return config;
    }

    public LevelConfig getLevelConfig() {
        return config.getLevelConfig();
    }

    public void startLevel() {
        gameEngine = new GameEngine(config, scene);
        gameEngine.start();
    }

}
