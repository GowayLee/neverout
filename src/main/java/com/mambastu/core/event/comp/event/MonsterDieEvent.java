package com.mambastu.core.event.comp.event;

import com.mambastu.material.pojo.entity.monster.BaseMonster;

import lombok.Getter;

public class MonsterDieEvent extends BaseEvent {
    private final static MonsterDieEvent INSTANCE = new MonsterDieEvent();

    @Getter
    private BaseMonster monster;

    public static MonsterDieEvent getInstance() {
        return INSTANCE;
    }

    private MonsterDieEvent() {
        super("MonsterDieEvent");
    }

    public void setProperty(BaseMonster monster) {
        this.monster = monster;
    }
}
