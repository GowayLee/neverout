package com.mambastu.core.logic.comp;

public interface ModeLogic {
    /*
     * 更新所有游戏实体属性
     */
    void update(long elapsedTime);

    void initCountDownTimer();

    void initMonsterGenTimer();

    void initPlayer();
}
