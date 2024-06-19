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
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

public abstract class BaseWeapon {
    protected enum Status {READY, COOLDOWN}; // 武器的状态，可以是准备就绪或冷却中。

    @Getter
    protected final SimpleIntegerProperty damage = new SimpleIntegerProperty(); // 子弹的伤害值
    @Getter @Setter
    protected double damageBuff = 1.0;
    @Getter
    protected final SimpleDoubleProperty bulletSpeed = new SimpleDoubleProperty();
    @Getter @Setter
    protected double bulletSpeedBuff = 1.0;
    @Getter
    protected final SimpleDoubleProperty coolTime = new SimpleDoubleProperty();
    @Getter @Setter
    protected double coolTimeBuff = 1.0;
    @Getter
    protected final SimpleDoubleProperty range = new SimpleDoubleProperty(); // 武器的射程
    @Getter @Setter
    protected double rangeBuff = 1.0;
    @Getter @Setter
    protected BulletType bulletType;
    protected Status coolStatus;
    
    @Getter
    protected final PauseTransition coolTimer = new PauseTransition();
    protected final List<BaseBullet> newBulletList = new ArrayList<>();

    public void updateBuffProperties(double damageBuff, double bulletSpeedBuff, double coolTimeBuff, double rangeBuff) { // 更新武器的属性值，包括伤害、子弹速度、冷却时间和射程。
        this.damageBuff = damageBuff;
        this.bulletSpeedBuff= bulletSpeedBuff;
        this.coolTimeBuff = coolTimeBuff;
        this.rangeBuff = rangeBuff;
    }

    public void updateValueProperties(int newDamage, double newBulletSpeed, double newCoolTime, double newRange, BulletType newBulletType) {
        this.damage.set((int) (newDamage * damageBuff)); // 更新伤害值
        this.bulletSpeed.set(newBulletSpeed * bulletSpeedBuff); // 更新子弹速度
        this.coolTime.set(newCoolTime * coolTimeBuff); // 更新冷却时间
        this.range.set(newRange * rangeBuff); // 更新射程
        this.bulletType = newBulletType; // 更新子弹类型
        coolTimer.setDuration(Duration.millis(coolTime.get()));
    }

    abstract public BaseEntity selectTarget(double x, double y, LinkedList<BaseMonster> monsters); // 在一群实体中选择目标 TODO: 空间划分

    abstract public List<BaseBullet> fire(double x, double y, LinkedList<BaseMonster> monsters, Set<GameInput> activeInputs);

}
