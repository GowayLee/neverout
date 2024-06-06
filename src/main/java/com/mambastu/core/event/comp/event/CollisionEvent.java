package com.mambastu.core.event.comp.event;

import com.mambastu.material.pojo.entity.monster.BaseMonster;
import com.mambastu.material.pojo.entity.player.BasePlayer;

import lombok.Getter;

public class CollisionEvent extends BaseEvent { // FIXME: 事件命名原则以及分类
    @Getter
    private final BasePlayer player;

    @Getter
    private final BaseMonster monster;

    public CollisionEvent(BasePlayer player, BaseMonster monster) {
        super("CollisionEvent");
        this.player = player;
        this.monster = monster;
    }
}
