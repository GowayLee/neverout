package com.mambastu.material.pojo.bound;

import com.mambastu.material.pojo.enums.CollisionState;

public interface Bound {
    CollisionState collisionState(Bound other);

    /**
     * 更改实体碰撞箱的尺寸
     * @param newX
     * @param newY
     */
    void reArrangeBoundSize(double newWidth, double newHeight);
}
