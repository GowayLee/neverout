package com.mambastu.core.event.comp.event;

import com.mambastu.material.pojo.entity.monster.BaseMonster;
import com.mambastu.material.pojo.entity.player.BasePlayer;

import lombok.Getter;

public class CollisionEvent extends BaseEvent { // TODO: 事件命名原则以及分类
    private final static CollisionEvent INSTANCE = new CollisionEvent();

    @Getter
    private BasePlayer player;

    @Getter
    private BaseMonster monster;

    public static CollisionEvent getInstance() {
        return INSTANCE;
    }

    private CollisionEvent() {
        super("CollisionEvent");
    }

    public void setProperty(BasePlayer player, BaseMonster monster) {
        this.player = player;
        this.monster = monster;
    }
}
