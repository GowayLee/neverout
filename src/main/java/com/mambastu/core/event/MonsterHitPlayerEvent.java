package com.mambastu.core.event;

import com.mambastu.annotation.EventInfo;
import com.mambastu.enums.EventType;
import com.mambastu.gameobjects.entity.monster.BaseMonster;
import com.mambastu.gameobjects.entity.player.BasePlayer;

import lombok.Getter;

@EventInfo(type = EventType.MonsterHitPlayer)
public class MonsterHitPlayerEvent extends BaseEvent { // TODO: 事件命名原则以及分类
    private final static MonsterHitPlayerEvent INSTANCE = new MonsterHitPlayerEvent();

    @Getter
    private BasePlayer player;

    @Getter
    private BaseMonster monster;

    public static MonsterHitPlayerEvent getInstance() {
        return INSTANCE;
    }

    private MonsterHitPlayerEvent() {
    }

    public void setProperty(BasePlayer player, BaseMonster monster) {
        this.player = player;
        this.monster = monster;
    }
}
