package com.mambastu.infuse.pojo.config;

import com.mambastu.infuse.pojo.enums.GameMode;

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
