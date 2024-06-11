package com.mambastu.material.pojo.entity.player;

import java.util.Set;

import com.mambastu.controller.input.comp.GameInput;
import com.mambastu.material.pojo.entity.BaseEntity;

import javafx.beans.property.SimpleIntegerProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BasePlayer extends BaseEntity {
    protected double speed = 5;
    protected SimpleIntegerProperty HP = new SimpleIntegerProperty(100);

    public void move(Set<GameInput> activeInputs) { // TODO: 边界问题
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

        x.set(x.get() + deltaX);
        y.set(y.get() + deltaY);

        // if (!(x.get() < 0) && (x.get() + deltaX < prevX)) x.set(x.get() + deltaX);
        // if (!(y.get() < 0))

        imageView.setX(x.get());
        imageView.setY(y.get());
    }

    public void setPos(double sceneWidth, double sceneHeight) { // 默认出生在屏幕中央
        this.x.set(sceneWidth / 2);
        this.y.set(sceneHeight / 2);
        imageView.setX(x.get());
        imageView.setY(y.get());
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
    }

    public boolean isDie() { // 判断玩家是否死亡
        if (HP.get() <= 0) {
            return true;
        }
        return false;
    }

    abstract public void die(); // 设置玩家死亡状态


    @Override
    public Bounds getBounds() {//返回圆形bound类
        double radius = Math.max(imageView.getFitWidth(), imageView.getFitHeight()) / 2;
        return new CircleBounds(x, y, radius,prevX,prevY);
    }

}
