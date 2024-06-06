package com.mambastu.core.logic;

import java.beans.PropertyChangeListener;

import com.mambastu.core.engine.GameEngine.EngineProps;
import com.mambastu.core.logic.comp.ModeLogic;
import com.mambastu.core.logic.comp.NormalImpl;
import com.mambastu.infuse.level.comp.GameMode;
import com.mambastu.infuse.level.comp.config.GlobalConfig;

public class LogicManager { // TODO: 尝试单例模式
    private GlobalConfig config;
    private EngineProps engineProps;

    private ModeLogic logiModule; // 游戏模式逻辑策略

    public LogicManager(EngineProps engineProps) { // 初始化逻辑管理器并选择具体模式逻辑策略实现
        this.engineProps = engineProps;
        this.config = engineProps.getConfig();
        pickLogiImpl(config.getGameMode());
    }

    private void pickLogiImpl(GameMode gameMode) {
        switch (gameMode) {
            case NORMAL:
                logiModule = new NormalImpl(engineProps);
                break;
            default:
                break;
        }
    }

    public void addPropertyListener(PropertyChangeListener listener) {
        logiModule.addPropertyListener(listener);
    }

    public void update(long elapsedTime) {
        logiModule.updateEntity(elapsedTime);
    }

    public void initEntity() {
        logiModule.initPlayer();
        logiModule.initMonsterGenTimer();
    }

}
