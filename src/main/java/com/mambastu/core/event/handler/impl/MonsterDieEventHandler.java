package com.mambastu.core.event.handler.impl;

import com.mambastu.core.event.MonsterDieEvent;
import com.mambastu.core.event.handler.EventHandler;

public class MonsterDieEventHandler implements EventHandler<MonsterDieEvent>{

    @Override
    public void handle(MonsterDieEvent event) {
        event.getMonster().die();
    }
}
