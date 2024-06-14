package com.mambastu.material.pojo.entity.bullet;

import com.mambastu.material.pojo.Interface.Movable;
import com.mambastu.material.pojo.entity.BaseEntity;
import com.mambastu.material.pojo.enums.CollisionState;
import javafx.beans.property.SimpleDoubleProperty;

import lombok.Getter;

public abstract class BaseBullet extends BaseEntity implements Movable {
    @Getter
    protected int damage; // 子弹的伤害值
    protected double speed;
    protected double range; // 子弹的射程，超出射程后消失
    // 以上参数需要由Weapon来赋予

    protected BaseEntity target; // 子弹的目标实体，用于追踪

    abstract public void move(); // 移动子弹的方法

    abstract public boolean isHitTarget();

    public boolean isOutRange() {
        return range <= 0; // 判断子弹是否超出射程，超出射程后消失
    }

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
    public Bounds getBounds() {// 返回圆形类
        double radius = Math.max(showingImageView.getFitWidth(), showingImageView.getFitHeight()) / 2;
        return new CircleBounds(x, y, radius, prevX, prevY);
    }

    @Override
    public void crossedBoundary() {
        double sceneWidth = root.getWidth();
        double sceneHeight = root.getHeight();
        RectangleOutBounds sceneBounds = new RectangleOutBounds(new SimpleDoubleProperty(0.0),
                new SimpleDoubleProperty(0.0), sceneWidth, sceneHeight);
        CollisionState collisionState = sceneBounds.collisionState(this.getBounds());
        if (collisionState == CollisionState.HORIZONTAL)
            x = new SimpleDoubleProperty(prevX);
        if (collisionState == CollisionState.VERTICAL)
            y = new SimpleDoubleProperty(prevY);
        if (collisionState == CollisionState.BOTH) {
            x = new SimpleDoubleProperty(prevX);
            y = new SimpleDoubleProperty(prevY);
        }
    }

    public void savePreviousFrame() {
        prevX = x.get();
        prevY = y.get();
    }

}