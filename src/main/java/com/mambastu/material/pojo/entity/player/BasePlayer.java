package com.mambastu.material.pojo.entity.player;

import java.util.Set;

import com.mambastu.controller.input.comp.GameInput;
import com.mambastu.material.pojo.entity.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BasePlayer extends BaseEntity {
    protected double speed = 5;
    protected int HP = 100;

    public void move(Set<GameInput> activeInputs) {
        double deltaX = 0, deltaY = 0;

        if (activeInputs.contains(GameInput.MOVE_UP))
            deltaY -= speed;
        if (activeInputs.contains(GameInput.MOVE_DOWN))
            deltaY += speed;
        if (activeInputs.contains(GameInput.MOVE_LEFT))
            deltaX -= speed;
        if (activeInputs.contains(GameInput.MOVE_RIGHT))
            deltaX += speed;

        if (deltaX != 0 && deltaY != 0) {
            deltaX /= Math.sqrt(2);
            deltaY /= Math.sqrt(2);
        }

        x += deltaX;
        y += deltaY;

        imageView.setX(x);
        imageView.setY(y);
    }

    public void setPos(double sceneWidth, double sceneHeight) { // 默认出生在屏幕中央
        this.x = sceneWidth / 2;
        this.y = sceneHeight / 2;
        imageView.setX(x);
        imageView.setY(y);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
    }

    public boolean isDie() { // 判断玩家是否死亡
        if (HP <= 0) {
            return true;
        }
        return false;
    }
    
    abstract public void die(); // 设置玩家死亡状态
}


