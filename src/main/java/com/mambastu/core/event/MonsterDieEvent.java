package com.mambastu.core.event;

import com.mambastu.annotation.EventInfo;
import com.mambastu.enums.EventType;
import com.mambastu.gameobjects.entity.monster.BaseMonster;

import lombok.Getter;

@EventInfo(type = EventType.MonsterDie)
public class MonsterDieEvent extends BaseEvent {
    private final static MonsterDieEvent INSTANCE = new MonsterDieEvent();

    @Getter
    private BaseMonster monster;

    public static MonsterDieEvent getInstance() {
        return INSTANCE;
    }

    private MonsterDieEvent() {
    }

    public void setProperty(BaseMonster monster) {
        this.monster = monster;
    }
}
