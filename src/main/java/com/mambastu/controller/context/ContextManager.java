package com.mambastu.controller.context;

import com.mambastu.controller.context.dto.Context;
import com.mambastu.controller.context.logic.ModeCtxLogic;
import com.mambastu.controller.context.logic.impl.ChallengeCtxImpl;
import com.mambastu.controller.context.logic.impl.NormalCtxImpl;
import com.mambastu.enums.GameMode;

public class ContextManager {
    private final Context ctx;

    private ModeCtxLogic modeCtxLogic;

    // ================================= init Section =================================
    public ContextManager(Context ctx) {
        this.ctx = ctx;
    }

    public void init() {
        pickLogicImpl(ctx.getGameMode().get()); // 选择逻辑实现类，这里使用的是NormalCtxImpl，可以根据需要进行修改。
        initFirstCtx(); // 初始化第一关的上下文信息。
    }

    private void initFirstCtx() { // 进入第一关前初始化关卡上下文信息，包括关卡参数、关卡记录等。
        ctx.initPlayer();
        modeCtxLogic.initLevelConfig();
        ctx.initLevelRecord();
        ctx.initGlobalRecord();
    }

    /**
     * Select the implementation class of Context updating based on the game mode.
     * @param gameMode   
     * @return void
     * @throws IllegalArgumentException if the game mode is not defined.
     */
    private void pickLogicImpl(GameMode gameMode) {
        switch (gameMode) {
            case NORMAL:
                modeCtxLogic = new NormalCtxImpl(ctx);
                break;
            case CHALLENGE:
                modeCtxLogic = new ChallengeCtxImpl(ctx);
                break;
            default:
                throw new IllegalArgumentException("Game mode not defined: " + gameMode);
        }
    }

    // ================================= Update Section =================================
    public void updateCtx() {
        modeCtxLogic.updateGlobalRecord();
        modeCtxLogic.updateLevelConfig(); // 更新下一关关卡参数
        modeCtxLogic.updatePlayerProp();
        modeCtxLogic.refreshLevelRecord(); // 刷新关卡记录。
    }

    public void updateCoin() {
        modeCtxLogic.updateCoin(); // 更新金币数量。
    }

    public void recordBeforeOver() {
        modeCtxLogic.recordBeforeOver(); // 记录关卡结束前的数据。
    }
}
