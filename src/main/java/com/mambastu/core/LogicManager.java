package com.mambastu.core;

import com.mambastu.controller.context.dto.Context;
import com.mambastu.controller.listener.LogicLayerListener;
import com.mambastu.core.GameEngine.GameRegistry;
import com.mambastu.core.logic.ModeLogic;
import com.mambastu.core.logic.impl.ChallengeImpl;
import com.mambastu.core.logic.impl.NormalImpl;
import com.mambastu.enums.GameMode;

public class LogicManager {
    private final Context ctx;
    private final GameRegistry gameRegistry;
    private final LogicLayerListener listener;

    private ModeLogic logiModule; // 游戏模式逻辑策略

    public LogicManager(GameRegistry gameRegistry, LogicLayerListener listener) { // 初始化逻辑管理器并选择具体模式逻辑策略实现
        this.listener = listener;
        this.gameRegistry = gameRegistry;
        this.ctx = gameRegistry.getCtx();
        pickLogiImpl(ctx.getGameMode().get());
    }

    
    /** 
     * Stratgey mode, choose different Logic Implement for different game mode
     * 
     * @param gameMode
     */
    private void pickLogiImpl(GameMode gameMode) {
        switch (gameMode) {
            case NORMAL:
                logiModule = new NormalImpl(gameRegistry, listener);
                break;
            case CHALLENGE:
                logiModule = new ChallengeImpl(gameRegistry, listener);
                break;
            default:
                break;
        }
    }

    /** 
     * Initialize Entities and Timers for the game mode.
     * 
     */
    public void initEntities() { // 初始化游戏实体，包括玩家、障碍物等
        logiModule.initPlayer();
    }

    /**
     * Initialize timers for the game mode.
     * 
     */
    public void initTimers() { // 初始化游戏计时器，包括怪物生成计时器、倒计时计时器等
        logiModule.initMonsterGenTimer();
        logiModule.initCountDownTimer();
    }

    public void update() {
        logiModule.update();
    }
}
