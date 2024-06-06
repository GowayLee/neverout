package com.mambastu.core.logic;

import com.mambastu.core.engine.GameEngine.EngineProps;
import com.mambastu.core.logic.comp.ModeLogic;
import com.mambastu.core.logic.comp.NormalImpl;
import com.mambastu.infuse.pojo.config.GlobalConfig;
import com.mambastu.infuse.pojo.enums.GameMode;

public class LogicManager {
    private GlobalConfig config;
    private EngineProps engineProps;

    private ModeLogic LogiModule; // 游戏模式逻辑策略

    public LogicManager(EngineProps engineProps) { // 初始化逻辑管理器并选择具体模式逻辑策略实现
        this.engineProps = engineProps;
        this.config = engineProps.getConfig();
        pickLogiImpl(config.getGameMode());
    }

    private void pickLogiImpl(GameMode gameMode) {
        switch (gameMode) {
            case NORMAL:
                LogiModule = new NormalImpl(engineProps);
                break;
            default:
                break;
        }
    }

    public void update(long elapsedTime) {
        LogiModule.updateEntity(elapsedTime);
        LogiModule.updateEngineState();
    }

    public void initEntity() {
        LogiModule.initPlayer();
        LogiModule.initMonsterGenTimer();
    }

}
