package com.mambastu.core.event.comp.handler;

import com.mambastu.core.event.comp.event.MonsterDieEvent;

public class MonsterDieEventHandler extends BaseEventHandler<MonsterDieEvent>{

    @Override
    public void handle(MonsterDieEvent event) {
        event.getMonster().die();
    }
}
