package com.mambastu.core.event.comp.handler;

import java.util.LinkedList;

import com.mambastu.core.event.comp.event.PlayerDieEvent;
import com.mambastu.material.pojo.entity.monster.BaseMonster;
import com.mambastu.material.pojo.entity.player.BasePlayer;

public class PlayerDieEventHandler extends BaseEventHandler<PlayerDieEvent>{
    @Override
    public void handle(PlayerDieEvent event) {
        System.out.println("handler!");
        BasePlayer player = event.getPlayer();
        player.die();

        LinkedList<BaseMonster> monsterList = event.getMonsterList();
        for (BaseMonster monster : monsterList) {
            monster.removeFromPane(event.getRoot());
        }
    }
}
