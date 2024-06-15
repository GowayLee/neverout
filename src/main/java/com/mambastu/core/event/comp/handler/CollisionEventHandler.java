package com.mambastu.core.event.comp.handler;

import com.mambastu.core.event.comp.event.CollisionEvent;

public class CollisionEventHandler extends BaseEventHandler<CollisionEvent> {
    @Override
    public void handle(CollisionEvent event) {
        int damage = event.getMonster().releaseDamage();
        if (damage > 0) {
            event.getPlayer().getHurt(damage);
        }
    }
}
