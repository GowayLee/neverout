package com.mambastu.gameobjects.bound;

import com.mambastu.enums.gameobjects.CollisionState;
import com.mambastu.utils.BetterMath;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;

public class ScreenBound { // 窗口边界
    private static final SimpleDoubleProperty width = new SimpleDoubleProperty();
    private static final SimpleDoubleProperty height = new SimpleDoubleProperty();

    public static void init(Pane root) {
        width.bind(root.widthProperty().add(20)); // 绑定到根节点的宽度属性上，当根节点宽度变化时，width属性也会更新
        height.bind(root.heightProperty().add(20)); // 绑定到根节点的高度属性上，当根节点高度变化时，height属性也会更新
    }

    public static CollisionState collisionState(Bound aim) {

        if (aim instanceof RectangleInBound) {// 和矩形Bound碰撞
            RectangleInBound aimRectangle = (RectangleInBound) aim;
            boolean collision = 0 < aimRectangle.getX().get() + aimRectangle.getWidth() &&
                    0 + width.get() > aimRectangle.getX().get() &&
                    0 < aimRectangle.getY().get() + aimRectangle.getHeight() &&
                    0 + height.get() > aimRectangle.getY().get();
            if (collision)
                return CollisionState.TRUE;
        } else if (aim instanceof CircleBound) {// 和圆形Bound碰撞
            CircleBound aimCircle = (CircleBound) aim;
            double cX = aimCircle.getX().get();
            double cY = aimCircle.getY().get();
            double cCenterX = aimCircle.getCenterX().get();
            double cCenterY = aimCircle.getCenterY().get();
            double cRadius = aimCircle.getRadius();
            double cPrevX = aimCircle.getPrevX();
            double cPrevY = aimCircle.getPrevY();
            double wallX = 20.0;
            double wallY = 20.0;
            double wallWidth = width.get();
            double wallHeight = height.get();

            boolean intersectsTop = cPrevY > cY && isImageViewCenterNearLine(cCenterX, cCenterY,
                    cRadius, wallX, wallY, wallX + wallWidth, wallY);
            boolean intersectsBottom = cPrevY < cY && isImageViewCenterNearLine(cCenterX, cCenterY,
                    cRadius, wallX, wallY + wallHeight, wallX + wallWidth, wallY + wallHeight);
            boolean intersectsLeft = cPrevX > cX && isImageViewCenterNearLine(cCenterX, cCenterY,
                    cRadius, wallX, wallY, wallX, wallY + wallHeight);
            boolean intersectsRight = cPrevX < cX && isImageViewCenterNearLine(cCenterX, cCenterY,
                    cRadius, wallX + wallWidth, wallY, wallX + wallWidth, wallY + wallHeight);

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

    private static double pointToLineDistance(double px, double py, double x1, double y1, double x2, double y2) { // TODO:
                                                                                                                  // 算法改进
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
