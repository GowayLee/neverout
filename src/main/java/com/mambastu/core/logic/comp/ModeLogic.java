package com.mambastu.core.logic.comp;


public interface ModeLogic {
    void updateEntity(long elapsedTime);

    void updateEngineState();

    void initMonsterGenTimer();

    void initPlayer();
}
