package com.mambastu.core.event.comp.event;

import java.util.LinkedList;

import com.mambastu.material.pojo.entity.monster.BaseMonster;
import com.mambastu.material.pojo.entity.player.BasePlayer;

import javafx.scene.layout.Pane;
import lombok.Getter;

public class PlayerDieEvent extends BaseEvent{
    @Getter
    private final BasePlayer player;

    @Getter
    private LinkedList<BaseMonster> monsterList;

    @Getter
    private Pane root;

    public PlayerDieEvent(BasePlayer player, LinkedList<BaseMonster> monsterList, Pane root) {
        super("PlayerDieEvent");
        this.player = player;
        this.monsterList = monsterList;
        this.root = root;
    }
}
