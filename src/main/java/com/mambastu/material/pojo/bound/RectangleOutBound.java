package com.mambastu.material.pojo.bound;

import com.mambastu.material.pojo.enums.CollisionState;

import javafx.beans.property.SimpleDoubleProperty;
import lombok.Getter;

@Getter
public class RectangleOutBound implements Bound {
    private final SimpleDoubleProperty x;
    private final SimpleDoubleProperty y;
    private double width;
    private double height;

    public RectangleOutBound(SimpleDoubleProperty x, SimpleDoubleProperty y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void reArrangeBoundSize(double newWidth, double newHeight) {
        this.width = newWidth;// 重新设置矩形Bound的宽高
        this.height = newHeight;// 重新设置矩形Bound的宽高
    }

    @Override
    public CollisionState collisionState(Bound aim) {

        if (aim instanceof RectangleInBound) {// 和矩形Bound碰撞
            RectangleInBound aimRectangle = (RectangleInBound) aim;
            boolean collision = x.get() < aimRectangle.getX().get() + aimRectangle.getWidth() &&
                    x.get() + width > aimRectangle.getX().get() &&
                    y.get() < aimRectangle.getY().get() + aimRectangle.getHeight() &&
                    y.get() + height > aimRectangle.getY().get();
            if (collision)
                return CollisionState.TRUE;
        } else if (aim instanceof CircleBound) {// 和圆形Bound碰撞
            CircleBound aimCircle = (CircleBound) aim;
            return aimCircle.collisionState(this);// 直接调用Bounds和矩形碰撞的方法
        }
        return CollisionState.FALSE;
    }
}
