package com.mambastu.controller.listener;

public interface LogicLayerListener {
    void pauseEngine();

    void resumeEngine();

    void stopEngine(boolean isPassLevel);
}
