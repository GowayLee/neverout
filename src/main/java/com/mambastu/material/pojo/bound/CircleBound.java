package com.mambastu.material.pojo.bound;

import com.mambastu.material.pojo.enums.CollisionState;
import com.mambastu.util.BetterMath;

import javafx.beans.property.SimpleDoubleProperty;
import lombok.Getter;

@Getter
public class CircleBound implements Bound{
    private final SimpleDoubleProperty x = new SimpleDoubleProperty();
    private final SimpleDoubleProperty y = new SimpleDoubleProperty();
    private final SimpleDoubleProperty centerX = new SimpleDoubleProperty();
    private final SimpleDoubleProperty centerY = new SimpleDoubleProperty();
    private double radius;
    private double prevX;
    private double prevY;

    public CircleBound(SimpleDoubleProperty x, SimpleDoubleProperty y, double radius, double prevX, double prevY) {
        this.x.bind(x);
        this.y.bind(y);
        this.centerX.bind(x.add(radius));
        this.centerY.bind(y.add(radius));
        this.prevX = prevX;
        this.prevY = prevY;
        this.radius = radius;
    }

    @Override
    public void reArrangeBoundSize(double newWidth, double newHeight) {
        double radius = Math.max(newWidth, newHeight) / 2;
        this.radius = radius;
    }


    @Override
    public CollisionState collisionState(Bound aim) {
        if (aim instanceof CircleBound) {// 判断和圆形Bound的碰撞
            CircleBound otherCircle = (CircleBound) aim;
            double dx = centerX.get() - otherCircle.getCenterX().get();
            double dy = centerY.get() - otherCircle.getCenterY().get();
            double distance = dx * dx + dy * dy;
            double radiusSum = (radius + otherCircle.getRadius()) * (radius + otherCircle.getRadius());
            if (distance < radiusSum) return CollisionState.TRUE;
        } else if (aim instanceof RectangleInBound) {// 判断和不可进入矩形Bound的碰撞
            RectangleInBound otherRectangle = (RectangleInBound) aim;
            double wallX = otherRectangle.getX().get();
            double wallY = otherRectangle.getY().get();
            double width = otherRectangle.getX().get() + otherRectangle.getWidth();
            double height = otherRectangle.getY().get() + otherRectangle.getHeight();
            boolean intersectsTop = prevY < y.get() && isImageViewCenterNearLine(centerX.get(), centerY.get(),
                    radius, wallX, wallY, wallX + width, wallY);
            boolean intersectsBottom = prevY > y.get() && isImageViewCenterNearLine(centerX.get(), centerY.get(),
                    radius, wallX, wallY + height, wallX + width, wallY + height);
            boolean intersectsLeft = prevX < x.get() && isImageViewCenterNearLine(centerX.get(), centerY.get(),
                    radius, wallX, wallY, wallX, wallY + height);
            boolean intersectsRight = prevX > x.get() && isImageViewCenterNearLine(centerX.get(), centerY.get(),
                    radius, wallX + width, wallY, wallX + width, wallY + height);

            if ((intersectsBottom || intersectsTop) && (intersectsLeft || intersectsRight))
                return CollisionState.BOTH;
            if ((intersectsBottom || intersectsTop))
                return CollisionState.VERTICAL;
            if ((intersectsLeft || intersectsRight))
                return CollisionState.HORIZONTAL;
        } else if (aim instanceof RectangleOutBound) {// 判断和不可离开矩形Bound的碰撞
            RectangleOutBound otherRectangle = (RectangleOutBound) aim;
            double wallX = otherRectangle.getX().get();
            double wallY = otherRectangle.getY().get();
            double width = otherRectangle.getX().get() + otherRectangle.getWidth();
            double height = otherRectangle.getY().get() + otherRectangle.getHeight();
            boolean intersectsTop = prevY > y.get() && isImageViewCenterNearLine(centerX.get(), centerY.get(),
                    radius, wallX, wallY, wallX + width, wallY);
            boolean intersectsBottom = prevY < y.get() && isImageViewCenterNearLine(centerX.get(), centerY.get(),
                    radius, wallX, wallY + height, wallX + width, wallY + height);
            boolean intersectsLeft = prevX > x.get() && isImageViewCenterNearLine(centerX.get(), centerY.get(),
                    radius, wallX, wallY, wallX, wallY + height);
            boolean intersectsRight = prevX < x.get() && isImageViewCenterNearLine(centerX.get(), centerY.get(),
                    radius, wallX + width, wallY, wallX + width, wallY + height);

            if ((intersectsBottom || intersectsTop) && (intersectsLeft || intersectsRight))
                return CollisionState.BOTH;
            if ((intersectsBottom || intersectsTop))
                return CollisionState.VERTICAL;
            if ((intersectsLeft || intersectsRight))
                return CollisionState.HORIZONTAL;
        }
        return CollisionState.FALSE;
    }

    public static boolean isImageViewCenterNearLine(double centerX, double centerY, double radius, double x1,
                                                    double y1, double x2, double y2) {
        // 计算线段到圆心的距离
        double distance = pointToLineDistance(centerX, centerY, x1, y1, x2, y2);

        // 如果距离小于或等于半径，则视为重合
        return distance <= radius;
    }

    private static double pointToLineDistance(double px, double py, double x1, double y1, double x2, double y2) { // TODO: 算法改进
        double A = px - x1;
        double B = py - y1;
        double C = x2 - x1;
        double D = y2 - y1;

        double dot = A * C + B * D;
        double lenSq = C * C + D * D;
        double param = -1;
        if (lenSq != 0) {
            param = dot / lenSq;
        }

        double nearestX, nearestY;

        if (param < 0) {
            nearestX = x1;
            nearestY = y1;
        } else if (param > 1) {
            nearestX = x2;
            nearestY = y2;
        } else {
            nearestX = x1 + param * C;
            nearestY = y1 + param * D;
        }
        double dx = px - nearestX;
        double dy = py - nearestY;
        return BetterMath.sqrt(dx * dx + dy * dy);
    }
}
