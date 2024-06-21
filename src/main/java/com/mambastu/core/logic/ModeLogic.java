package com.mambastu.core.logic;

public interface ModeLogic {
    /*
     * Update game datas for each frame
     * 
     */
    void update();

    /**
     * Initialize count down timer.
     * 
     */
    void initCountDownTimer();

    void initMonsterGenTimer();

    void initPlayer();
}
