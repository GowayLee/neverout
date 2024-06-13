package com.mambastu.core.event.comp.handler;

import com.mambastu.core.event.comp.event.BulletHitMonsterEvent;

public class BulletHitMonsterEventHandler extends BaseEventHandler<BulletHitMonsterEvent> {

    @Override
    public void handle(BulletHitMonsterEvent event) {
        event.getMonster().getHurt(event.getBullet().getDamage());
    }
}
