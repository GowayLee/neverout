package com.mambastu.core.handler;

import com.mambastu.core.event.MonsterHitPlayerEvent;

public class MonsterHitPlayerEventHandler implements EventHandler<MonsterHitPlayerEvent> {
    @Override
    public void handle(MonsterHitPlayerEvent event) {
        int damage = event.getMonster().releaseDamage();
        if (damage > 0) {
             event.getPlayer().getHurt(damage);
        }
    }
}
