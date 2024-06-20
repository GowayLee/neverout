package com.mambastu.core.handler;

import com.mambastu.core.event.PlayerDieEvent;
import com.mambastu.material.pojo.entity.player.BasePlayer;

public class PlayerDieEventHandler implements EventHandler<PlayerDieEvent>{
    @Override
    public void handle(PlayerDieEvent event) {
        BasePlayer player = event.getPlayer();
        player.die();
    }
}
