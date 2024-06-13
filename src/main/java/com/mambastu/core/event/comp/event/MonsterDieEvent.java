package com.mambastu.core.event.comp.event;

import com.mambastu.material.pojo.entity.monster.BaseMonster;

import javafx.scene.layout.Pane;
import lombok.Getter;

public class MonsterDieEvent extends BaseEvent{
    @Getter
    private final BaseMonster monster;

    @Getter
    private final Pane root; // 用于移除怪物节点

    public MonsterDieEvent(BaseMonster monster, Pane root) {
        super("MonsterDieEvent");
        this.monster = monster;
        this.root = root;
    }
}
