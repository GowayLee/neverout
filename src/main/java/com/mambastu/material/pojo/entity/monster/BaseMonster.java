package com.mambastu.material.pojo.entity.monster;

import java.util.Random;

import com.mambastu.material.pojo.entity.BaseEntity;
import com.mambastu.material.pojo.entity.player.BasePlayer;


public abstract class BaseMonster extends BaseEntity{
    protected double speed;
    abstract public void move(double targX, double targY); // 怪物的移动需要参照目标
    
    public void setPos(double sceneWidth, double sceneHeight, BasePlayer player) { // 默认在地图中随机生成 TODO: 避开玩家
        Random rand = new Random();
        this.x = rand.nextDouble() * sceneWidth;
        this.y = rand.nextDouble() * sceneHeight;
        imageView.setX(x);
        imageView.setY(y);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
    }

}
