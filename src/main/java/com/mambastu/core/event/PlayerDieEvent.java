package com.mambastu.core.event;

import com.mambastu.annotation.EventInfo;
import com.mambastu.enums.EventType;
import com.mambastu.material.pojo.entity.player.BasePlayer;

import lombok.Getter;

@EventInfo(type = EventType.PlayerDie)
public class PlayerDieEvent extends BaseEvent {
    private final static PlayerDieEvent INSTANCE = new PlayerDieEvent();

    @Getter
    private BasePlayer player;

    public static PlayerDieEvent getInstance() {
        return INSTANCE;
    }

    private PlayerDieEvent() {
    }

    public void setProperty(BasePlayer player) {
        this.player = player;
    }
}
