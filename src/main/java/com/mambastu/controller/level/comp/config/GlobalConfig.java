package com.mambastu.controller.level.comp.config;

import com.mambastu.controller.level.comp.GameMode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GlobalConfig {
    private GameMode gameMode = GameMode.NORMAL;
    private LevelConfig levelConfig;

    public GlobalConfig() {
        levelConfig = new LevelConfig();
    }

}
