package com.mambastu.listener;

import com.mambastu.controller.level.context.enums.GameMode;

public interface MainMenuListener{
    void selectGameMode(GameMode gameMode);
    
    void startGame();
}
