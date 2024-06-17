package com.mambastu.core.event.comp.event;

import com.mambastu.material.pojo.entity.bullet.BaseBullet;
import com.mambastu.material.pojo.entity.monster.BaseMonster;

import javafx.scene.layout.Pane;
import lombok.Getter;

public class BulletHitMonsterEvent extends BaseEvent {
    private final static BulletHitMonsterEvent INSTANCE = new BulletHitMonsterEvent();

    @Getter
    private BaseBullet bullet;

    @Getter
    private BaseMonster monster;

    @Getter
    private Pane root;

    public static BulletHitMonsterEvent getInstance() {
        return INSTANCE;
    }

    public BulletHitMonsterEvent() {

        super("BulletHitMonsterEvent");
    }

    public void setProperty(BaseBullet bullet, BaseMonster monster, Pane root) {
        this.bullet = bullet;
        this.monster = monster;
        this.root = root;
    }
}
