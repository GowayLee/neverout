package com.mambastu.material.pojo.entity.monster;

import java.util.Random;

import com.mambastu.material.pojo.entity.BaseEntity;
import com.mambastu.material.pojo.entity.player.BasePlayer;


public abstract class BaseMonster extends BaseEntity{
    protected double speed;
    abstract public void move(double targX, double targY); // 怪物的移动需要参照目标

    public void setPos(double sceneWidth, double sceneHeight, BasePlayer player) { // 默认在地图中随机生成 TODO: 避开玩家
        Random rand = new Random();
        // 怪物避开玩家的半径
        double avoidRadius = 50;
        do {
            x.set(rand.nextDouble() * sceneWidth);
            y.set(rand.nextDouble() * sceneHeight);
        } while (isInAvoidRange(player.getX().get(), player.getY().get(), avoidRadius));
        imageView.setX(x.get());
        imageView.setY(y.get());
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
    }

    private boolean isInAvoidRange(double playerX, double playerY, double avoidRadius) {
        double dx = x.get() - playerX;
        double dy = y.get() - playerY;
        double distanceSquared = dx * dx + dy * dy;
        return distanceSquared < avoidRadius * avoidRadius;
    }

    @Override
    public Bounds getBounds() {//返回圆形bound类
        double radius = Math.max(imageView.getFitWidth(), imageView.getFitHeight()) / 2;
        return new CircleBounds(x, y, radius);
    }

}
