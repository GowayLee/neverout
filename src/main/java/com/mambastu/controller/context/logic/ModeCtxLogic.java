package com.mambastu.controller.context.logic;

public interface ModeCtxLogic {

    void initLevelConfig();

    void updateLevelConfig();

    /**
     * update player's property, such as damage, MaxHP, etc.
     * 
     */
    void updatePlayerProp();

    void updateGlobalRecord();

    void updateCoin();

    void recordBeforeOver();

    void refreshLevelRecord();
}
