package com.mambastu.material.pojo.entity.bullet;

import com.mambastu.material.pojo.entity.BaseEntity;

public abstract class BaseBullet extends BaseEntity{

    @Override
    public Bounds getBounds() {//返回圆形类
        double radius = Math.max(imageView.getFitWidth(), imageView.getFitHeight()) / 2;
        return new CircleBounds(x, y, radius,prevX,prevY);
    }

}