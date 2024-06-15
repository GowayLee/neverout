package com.mambastu.material.pojo.weapon;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.mambastu.controller.input.comp.GameInput;
import com.mambastu.material.pojo.entity.BaseEntity;
import com.mambastu.material.pojo.entity.bullet.BaseBullet;
import com.mambastu.material.pojo.entity.bullet.BulletType;
import com.mambastu.material.pojo.entity.monster.BaseMonster;

import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.Pane;
import lombok.Getter;

public abstract class BaseWeapon {
    protected enum Status {READY, COOLDOWN}; // 武器的状态，可以是准备就绪或冷却中。

    @Getter
    protected final SimpleIntegerProperty damage = new SimpleIntegerProperty(); // 子弹的伤害值
    @Getter
    protected final SimpleDoubleProperty bulletSpeed = new SimpleDoubleProperty();
    @Getter
    protected final SimpleDoubleProperty coolTime = new SimpleDoubleProperty();
    @Getter
    protected final SimpleDoubleProperty range = new SimpleDoubleProperty(); // 武器的射程

    protected BulletType bulletType;
    protected Status coolStatus;
    protected final PauseTransition coolTimer = new PauseTransition();
    protected final List<BaseBullet> newBulletList = new ArrayList<>();

    abstract public BaseEntity selectTarget(double x, double y, LinkedList<BaseMonster> monsters); // 在一群实体中选择目标 TODO: 空间划分

    abstract public List<BaseBullet> fire(double x, double y, LinkedList<BaseMonster> monsters, Set<GameInput> activeInputs, Pane root);

}
