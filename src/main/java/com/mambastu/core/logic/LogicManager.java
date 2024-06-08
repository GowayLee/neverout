package com.mambastu.core.logic;

import com.mambastu.controller.level.comp.GameMode;
import com.mambastu.controller.level.comp.config.GlobalConfig;
import com.mambastu.core.engine.GameEngine.EngineProps;
import com.mambastu.core.logic.comp.ModeLogic;
import com.mambastu.core.logic.comp.NormalImpl;
import com.mambastu.listener.LogicLayerListener;

public class LogicManager { // TODO: 尝试单例模式
    private final GlobalConfig config;
    private final EngineProps engineProps;
    private final LogicLayerListener listener;

    private ModeLogic logiModule; // 游戏模式逻辑策略

    public LogicManager(EngineProps engineProps, LogicLayerListener listener) { // 初始化逻辑管理器并选择具体模式逻辑策略实现
        this.listener = listener;
        this.engineProps = engineProps;
        this.config = engineProps.getConfig();
        pickLogiImpl(config.getGameMode());
    }

    private void pickLogiImpl(GameMode gameMode) {
        switch (gameMode) {
            case NORMAL:
                logiModule = new NormalImpl(engineProps, listener);
                break;
            default:
                break;
        }
    }

    public void update(long elapsedTime) {
        logiModule.updateEntity(elapsedTime);
    }

    public void initEntity() {
        logiModule.initPlayer();
        logiModule.initMonsterGenTimer();
    }

}
