package com.mambastu.material.pojo.weapon;

import java.util.LinkedList;
import java.util.Set;

import com.mambastu.controller.input.comp.GameInput;
import com.mambastu.material.pojo.entity.BaseEntity;
import com.mambastu.material.pojo.entity.bullet.BaseBullet;
import com.mambastu.material.pojo.entity.bullet.BulletType;
import com.mambastu.material.pojo.entity.monster.BaseMonster;

import javafx.animation.Timeline;
import javafx.scene.layout.Pane;

public abstract class BaseWeapon {
    protected enum Status {READY, COOLING}; // 武器的状态，可以是准备就绪或冷却中。

    protected int damage; // 子弹的伤害值
    protected double bulletSpeed;
    protected double coolTime;
    protected double range; // 武器的射程

    protected BulletType bulletType;
    protected Status coolStatus;
    protected final Timeline coolTimer = new Timeline();

    protected void setStatus(Status status) {
        coolStatus = status;
    }

    abstract public BaseEntity selectTarget(double x, double y, LinkedList<BaseMonster> monsters); // 在一群实体中选择目标 TODO: 空间划分

    abstract public BaseBullet fire(double x, double y, LinkedList<BaseMonster> monsters, Set<GameInput> activeInputs, Pane root);

}
