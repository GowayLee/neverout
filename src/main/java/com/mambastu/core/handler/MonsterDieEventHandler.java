package com.mambastu.core.handler;

import com.mambastu.core.event.MonsterDieEvent;

public class MonsterDieEventHandler implements EventHandler<MonsterDieEvent>{

    @Override
    public void handle(MonsterDieEvent event) {
        event.getMonster().die();
    }
}
