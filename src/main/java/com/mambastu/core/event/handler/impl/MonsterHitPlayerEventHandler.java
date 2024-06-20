package com.mambastu.core.event.handler.impl;

import com.mambastu.core.event.MonsterHitPlayerEvent;
import com.mambastu.core.event.handler.EventHandler;

public class MonsterHitPlayerEventHandler implements EventHandler<MonsterHitPlayerEvent> {
    @Override
    public void handle(MonsterHitPlayerEvent event) {
        int damage = event.getMonster().releaseDamage();
        if (damage > 0) {
             event.getPlayer().getHurt(damage);
        }
    }
}
