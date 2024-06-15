package com.mambastu.material.pojo.entity.bullet;

import com.mambastu.material.pojo.bound.CircleBound;
import com.mambastu.material.pojo.entity.BaseEntity;
import javafx.scene.layout.Pane;
import lombok.Getter;

public abstract class BaseBullet extends BaseEntity{
    @Getter
    protected int damage; // 子弹的伤害值
    protected double speed;
    protected double range; // 子弹的射程，超出射程后消失
    // 以上参数需要由Weapon来赋予

    protected BaseEntity target; // 子弹的目标实体，用于追踪
    @Getter
    protected boolean isValid; // 子弹是否有效，用于判断是否需要移除子弹

    public BaseBullet() {
        this.bound = new CircleBound(x, y, 50, prevX, prevY);
    }

    abstract public void move(Pane root); // 移动子弹的方法

    /**
     * 子弹击中目标后的处理方法，例如爆炸效果等
     */
    abstract public void afterHitTarget();

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

    public Integer releaseDamage() {
        return damage;
    }

}