package com.mambastu.core.event.comp.event;

import com.mambastu.material.pojo.entity.monster.BaseMonster;

import javafx.scene.layout.Pane;
import lombok.Getter;

public class MonsterDieEvent extends BaseEvent {
    private final static MonsterDieEvent INSTANCE = new MonsterDieEvent();

    @Getter
    private BaseMonster monster;

    @Getter
    private Pane root; // 用于移除怪物节点

    public static MonsterDieEvent getInstance() {
        return INSTANCE;
    }

    private MonsterDieEvent() {
        super("MonsterDieEvent");
    }

    public void setProperty(BaseMonster monster, Pane root) {
        this.monster = monster;
        this.root = root;
    }
}
