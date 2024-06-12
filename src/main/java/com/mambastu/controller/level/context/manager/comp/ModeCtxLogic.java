package com.mambastu.controller.level.context.manager.comp;

import com.mambastu.material.pojo.entity.player.BasePlayer;

public interface ModeCtxLogic {
    void initLevelConfig(BasePlayer player);

    void updateLevelConfig();

    void initLevelRecord();

    void refreshLevelRecord();
}
