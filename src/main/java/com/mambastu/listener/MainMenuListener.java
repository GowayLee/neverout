package com.mambastu.listener;

import com.mambastu.controller.level.comp.GameMode;

public interface MainMenuListener{
    void selectGameMode(GameMode gameMode);
    
    void startGame();
}
