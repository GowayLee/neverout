package com.mambastu.core;

import com.mambastu.controller.context.dto.Context;
import com.mambastu.controller.listener.LogicLayerListener;
import com.mambastu.core.GameEngine.EngineProps;
import com.mambastu.core.logic.ModeLogic;
import com.mambastu.core.logic.impl.ChallengeImpl;
import com.mambastu.core.logic.impl.NormalImpl;
import com.mambastu.enums.GameMode;

public class LogicManager { // TODO: 尝试单例模式
    private final Context ctx;
    private final EngineProps engineProps;
    private final LogicLayerListener listener;

    private ModeLogic logiModule; // 游戏模式逻辑策略

    public LogicManager(EngineProps engineProps, LogicLayerListener listener) { // 初始化逻辑管理器并选择具体模式逻辑策略实现
        this.listener = listener;
        this.engineProps = engineProps;
        this.ctx = engineProps.getCtx();
        pickLogiImpl(ctx.getGameMode().get());
    }

    
    /** 
     * @param gameMode
     */
    private void pickLogiImpl(GameMode gameMode) {
        switch (gameMode) {
            case NORMAL:
                logiModule = new NormalImpl(engineProps, listener);
                break;
            case CHALLENGE:
                logiModule = new ChallengeImpl(engineProps, listener);
                break;
            default:
                break;
        }
    }

    public void initEntities() { // 初始化游戏实体，包括玩家、障碍物等
        logiModule.initPlayer();
    }

    public void initTimers() { // 初始化游戏计时器，包括怪物生成计时器、倒计时计时器等
        logiModule.initMonsterGenTimer();
        logiModule.initCountDownTimer();
    }

    public void update() {
        logiModule.update();
    }
}
