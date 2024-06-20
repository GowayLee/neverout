package com.mambastu.core.event;

import com.mambastu.annotation.EventInfo;
import com.mambastu.enums.EventType;
import com.mambastu.gameobjects.entity.bullet.BaseBullet;
import com.mambastu.gameobjects.entity.monster.BaseMonster;

import lombok.Getter;

@EventInfo(type = EventType.BulletHitMonster)
public class BulletHitMonsterEvent extends BaseEvent {
    private final static BulletHitMonsterEvent INSTANCE = new BulletHitMonsterEvent();

    @Getter
    private BaseBullet bullet;

    @Getter
    private BaseMonster monster;

    public static BulletHitMonsterEvent getInstance() {
        return INSTANCE;
    }

    private BulletHitMonsterEvent() {
    }

    public void setProperty(BaseBullet bullet, BaseMonster monster) {
        this.bullet = bullet;
        this.monster = monster;
    }
}
