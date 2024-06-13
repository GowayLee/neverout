package com.mambastu.core.event.comp.event;

import com.mambastu.material.pojo.entity.bullet.BaseBullet;
import com.mambastu.material.pojo.entity.monster.BaseMonster;
import com.mambastu.material.pojo.entity.player.BasePlayer;

import lombok.Getter;

public class BulletHitMonsterEvent extends BaseEvent{
    @Getter
    private final BaseBullet bullet;

    @Getter
    private final BaseMonster monster;

    public BulletHitMonsterEvent(BaseBullet bullet, BaseMonster monster) {
        super("BulletHitMonsterEvent");
        this.bullet = bullet;
        this.monster = monster;
    }
}
