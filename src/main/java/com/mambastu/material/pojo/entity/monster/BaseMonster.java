package com.mambastu.material.pojo.entity.monster;

import java.util.Random;

import com.mambastu.material.pojo.entity.BaseEntity;
import com.mambastu.material.pojo.entity.player.BasePlayer;


public abstract class BaseMonster extends BaseEntity{
    protected double speed;
    abstract public void move(double targX, double targY); // 怪物的移动需要参照目标

    public void setPos(double sceneWidth, double sceneHeight, BasePlayer player) { // 默认在地图中随机生成 TODO: 避开玩家
        Random rand = new Random();
        double x, y;
        // 怪物避开玩家的半径
        double avoidRadius = 50;
        do {
            x = rand.nextDouble() * sceneWidth;
            y = rand.nextDouble() * sceneHeight;
        } while (isInAvoidRange(x, y, player.getX(), player.getY(), avoidRadius));

        this.x = x;
        this.y = y;
        imageView.setX(x);
        imageView.setY(y);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
    }
    private boolean isInAvoidRange(double x, double y, double playerX, double playerY, double avoidRadius) {
        double dx = x - playerX;
        double dy = y - playerY;
        double distanceSquared = dx * dx + dy * dy;
        return distanceSquared < avoidRadius * avoidRadius;
    }
}
