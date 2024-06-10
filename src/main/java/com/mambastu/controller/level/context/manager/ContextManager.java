package com.mambastu.controller.level.context.manager;

import com.mambastu.controller.level.context.dto.Context;
import com.mambastu.controller.level.context.enums.GameMode;
import com.mambastu.controller.level.context.manager.comp.ModeCtxLogic;
import com.mambastu.controller.level.context.manager.comp.NormalCtxImpl;
import com.mambastu.material.factories.PlayerFactory;
import com.mambastu.material.pojo.entity.player.BasePlayer;

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
        modeCtxLogic.initLevelConfig(initPlayer());
        modeCtxLogic.refreshLevelRecord();
    }

    private BasePlayer initPlayer() { // 根据玩家选择进入第一关生成玩家
        BasePlayer player =  PlayerFactory.getInstance().create(ctx.getPlayerType().get());
        player.init();
        return player;
    }

    private void pickLogicImpl(GameMode mode) {
        switch (mode) {
            case NORMAL:
                modeCtxLogic = new NormalCtxImpl(ctx);
                break;
        
            default:
                break;
        }
    }

    // ================================= Update Section =================================
    public void updateCtx() {
        modeCtxLogic.updateLevelConfig(); // 更新下一关关卡参数。
        modeCtxLogic.refreshLevelRecord(); // 刷新关卡记录。
    }

    public void updateGlobalRecord() { // TODO: 更新全局记录，包括玩家最高得分、最高等级等。
        
    }

}
