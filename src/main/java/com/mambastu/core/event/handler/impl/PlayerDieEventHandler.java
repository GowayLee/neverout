package com.mambastu.core.event.handler.impl;

import com.mambastu.core.event.PlayerDieEvent;
import com.mambastu.core.event.handler.EventHandler;
import com.mambastu.gameobjects.entity.player.BasePlayer;

public class PlayerDieEventHandler implements EventHandler<PlayerDieEvent>{
    @Override
    public void handle(PlayerDieEvent event) {
        BasePlayer player = event.getPlayer();
        player.die();
    }
}
