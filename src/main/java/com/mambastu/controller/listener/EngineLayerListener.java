package com.mambastu.controller.listener;

public interface EngineLayerListener {
    void pauseGame();
    
    void resumeGame();

    void stopGame(boolean isPassLevel);
}
