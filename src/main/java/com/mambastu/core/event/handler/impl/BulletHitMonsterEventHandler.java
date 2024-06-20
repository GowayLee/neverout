package com.mambastu.core.event.handler.impl;

import com.mambastu.core.event.BulletHitMonsterEvent;
import com.mambastu.core.event.handler.EventHandler;

public class BulletHitMonsterEventHandler implements EventHandler<BulletHitMonsterEvent> {

    @Override
    public void handle(BulletHitMonsterEvent event) {
        event.getMonster().getHurt(event.getBullet());
        event.getBullet().afterHitTarget();
    }
}
