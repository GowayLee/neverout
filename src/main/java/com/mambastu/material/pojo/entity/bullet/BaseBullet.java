package com.mambastu.material.pojo.entity.bullet;

import com.mambastu.material.pojo.entity.BaseEntity;

import lombok.Getter;

public abstract class BaseBullet extends BaseEntity{
    @Getter
    protected int damage; // 子弹的伤害值
    protected double speed;
    protected double range; // 子弹的射程，超出射程后消失
    // 以上参数需要由Weapon来赋予

    protected BaseEntity target; // 子弹的目标实体，用于追踪
    
    abstract public void move(); // 移动子弹的方法

    abstract public boolean isHitTarget();

    public void setProps(int damage, double speed, double range) {
        this.damage = damage;
        this.speed = speed;
        this.range = range;
    }

    public void setPos(double weaponX, double weaponY) { // 默认出生在生成者位置，传入生成者坐标
        this.x.set(weaponX);
        this.y.set(weaponY);
        showingImageView.setX(x.get());
        showingImageView.setY(y.get());
    }

    public void setTarget(BaseEntity target) { // 设置目标实体，用于追踪
        this.target = target;
    }

    @Override
    public Bounds getBounds() {//返回圆形类
        double radius = Math.max(showingImageView.getFitWidth(), showingImageView.getFitHeight()) / 2;
        return new CircleBounds(x, y, radius);
    }

}