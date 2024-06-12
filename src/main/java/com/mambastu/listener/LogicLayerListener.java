package com.mambastu.listener;

public interface LogicLayerListener {
    void pauseEngine();

    void resumeEngine();

    void stopEngine(boolean isPassLevel);
}
