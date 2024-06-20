package com.mambastu.core.handler;

import com.mambastu.core.event.BulletHitMonsterEvent;

public class BulletHitMonsterEventHandler implements EventHandler<BulletHitMonsterEvent> {

    @Override
    public void handle(BulletHitMonsterEvent event) {
        event.getMonster().getHurt(event.getBullet());
        event.getBullet().afterHitTarget();
    }
}
