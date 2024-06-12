package com.mambastu.controller.level.context.manager.comp;

public interface ModeCtxLogic {

    void initLevelConfig();

    void updateLevelConfig();

    void updateGlobalRecord();

    void recordBeforeOver();

    void refreshLevelRecord();
}
