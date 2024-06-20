package com.mambastu.gameobjects.bound;

import com.mambastu.enums.gameobjects.CollisionState;

public interface Bound {
    CollisionState collisionState(Bound other);

    /**
     * 更改实体碰撞箱的尺寸
     * @param newX
     * @param newY
     */
    void reArrangeBoundSize(double newWidth, double newHeight);
}
