package com.mambastu.core.logic.comp;

public interface ModeLogic {
    /*
     * 更新所有游戏实体属性
     */
    void updateEntity(long elapsedTime);

    void initMonsterGenTimer();

    void initPlayer();
}
