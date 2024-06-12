package com.mambastu.core.event.comp.handler;

import com.mambastu.core.event.comp.event.PlayerDieEvent;
import com.mambastu.material.pojo.entity.player.BasePlayer;

public class PlayerDieEventHandler extends BaseEventHandler<PlayerDieEvent>{
    @Override
    public void handle(PlayerDieEvent event) {
        BasePlayer player = event.getPlayer();
        player.die();
    }
}
