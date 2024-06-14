package com.mambastu.core.event.comp.event;

import com.mambastu.material.pojo.entity.player.BasePlayer;

import lombok.Getter;

public class PlayerDieEvent extends BaseEvent {
    private final static PlayerDieEvent INSTANCE = new PlayerDieEvent();

    @Getter
    private BasePlayer player;

    public static PlayerDieEvent getInstance() {
        return INSTANCE;
    }

    private PlayerDieEvent() {
        super("PlayerDieEvent");
    }

    public void setProperty(BasePlayer player) {
        this.player = player;
    }
}
