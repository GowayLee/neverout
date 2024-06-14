package com.mambastu.core.event.comp.event;

import com.mambastu.material.pojo.entity.bullet.BaseBullet;
import com.mambastu.material.pojo.entity.monster.BaseMonster;

import lombok.Getter;

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
        super("BulletHitMonsterEvent");
    }

    public void setProperty(BaseBullet bullet, BaseMonster monster) {
        this.bullet = bullet;
        this.monster = monster;
    }
}
