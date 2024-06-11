package com.mambastu.material.pojo.entity;

import com.mambastu.material.pojo.entity.enums.CollisionState;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseEntity {
    protected final ImageView showingImageView = new ImageView(); // 正在被展示的图片节点
    protected final SimpleObjectProperty<Image> showingImage = new SimpleObjectProperty<>(); // 正在被绑定展示的图片
    protected SimpleDoubleProperty x = new SimpleDoubleProperty();
    protected SimpleDoubleProperty y = new SimpleDoubleProperty();
    protected double prevX; // 用于碰撞检测的变量，记录上一次的位置
    protected double prevY;

    abstract public void init(); // 更新实体的位置和状态等

    public void setImageSize(double width, double height) {
        showingImageView.setFitWidth(width);
        showingImageView.setFitHeight(height);
    }

    public void putOnPane (Pane root) { // 放置实体
        root.getChildren().add(showingImageView);
    }

    public void removeFromPane (Pane root) { // 移除实体
        root.getChildren().remove(showingImageView);
    }



    //Bounds内部类,用于碰撞检测
    public abstract static class Bounds {
        public abstract CollisionState collisionState(Bounds other);
    }

      //矩形Bounds类
    public static class RectangleBounds extends Bounds {
        private final SimpleDoubleProperty x;
        private final SimpleDoubleProperty y;
        private final double width;
        private final double height;



        public RectangleBounds(SimpleDoubleProperty x, SimpleDoubleProperty y, double width, double height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }


        public SimpleDoubleProperty getX() {return x;}
        public SimpleDoubleProperty getY() {return y;}
        public double getWidth() {return width;}
        public double getHeight() {return height;}


        @Override
        public CollisionState collisionState(Bounds aim) {

            if (aim instanceof RectangleBounds) {//和矩形Bound碰撞
                RectangleBounds aimRectangle = (RectangleBounds) aim;
                boolean collision =
                        x.get() < aimRectangle.getX().get() + aimRectangle.getWidth() &&
                        x.get() + width > aimRectangle.getX().get() &&
                        y.get() < aimRectangle.getY().get() + aimRectangle.getHeight() &&
                        y.get() + height > aimRectangle.getY().get();
                if(collision)return CollisionState.TRUE;
            }
            else if (aim instanceof CircleBounds) {//和圆形Bound碰撞
                CircleBounds aimCircle = (CircleBounds) aim;
                return aimCircle.collisionState(this);
            }
            return CollisionState.FALSE;
        }
    }




    //圆形Bounds
    public static class CircleBounds extends Bounds {
        private final SimpleDoubleProperty x = new SimpleDoubleProperty();
        private final SimpleDoubleProperty y = new SimpleDoubleProperty();
        private final SimpleDoubleProperty centerX = new SimpleDoubleProperty();
        private final SimpleDoubleProperty centerY = new SimpleDoubleProperty();
        private final double radius;
        private double prevX;
        private double prevY;


        public CircleBounds(SimpleDoubleProperty x, SimpleDoubleProperty y, double radius,double prevX,double prevY) {
            this.x.bind(x);
            this.y.bind(y);
            this.centerX.bind(x.add(radius));
            this.centerY.bind(y.add(radius));
            this.prevX=prevX;
            this.prevY=prevY;
            this.radius = radius;
        }


        public SimpleDoubleProperty getCenterX() {return centerX;}
        public SimpleDoubleProperty getCenterY() {return centerY;}
        public SimpleDoubleProperty getX() {return x;}
        public SimpleDoubleProperty getY() {return y;}
        public double getPrevX() {return prevX;}
        public double getPrevY() {return prevY;}
        public double getRadius() {return radius;}


        @Override
        public CollisionState collisionState(Bounds aim) {
            if (aim instanceof CircleBounds) {//判断和圆形Bound的碰撞
                CircleBounds otherCircle = (CircleBounds) aim;
                double dx = centerX.get() - otherCircle.getCenterX().get();
                double dy = centerY.get() - otherCircle.getCenterY().get();
                double distance = Math
                        .sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
                double radiusSum = radius + otherCircle.getRadius();
                if(distance< radiusSum) return CollisionState.TRUE;
            }
            else if (aim instanceof RectangleBounds) {//判断和矩形Bound的碰撞
                RectangleBounds otherRectangle = (RectangleBounds) aim;
                double wallX = otherRectangle.getX().get();
                double wallY = otherRectangle.getY().get();
                double width = otherRectangle.getX().get() + otherRectangle.getWidth();
                double height = otherRectangle.getY().get() + otherRectangle.getHeight();
                boolean intersectsTop =
                        prevY < y.get() && isImageViewCenterNearLine(centerX.get(),centerY.get(),radius,wallX, wallY, wallX + width, wallY);
                boolean intersectsBottom =
                        prevY > y.get() && isImageViewCenterNearLine(centerX.get(),centerY.get(), radius,wallX, wallY + height, wallX + width, wallY + height);
                boolean intersectsLeft =
                        prevX < x.get() && isImageViewCenterNearLine(centerX.get(),centerY.get(),radius, wallX, wallY, wallX, wallY + height);
                boolean intersectsRight =
                        prevX > x.get() && isImageViewCenterNearLine(centerX.get(),centerY.get(), radius,wallX + width, wallY, wallX + width, wallY + height);

                if ((intersectsBottom || intersectsTop)&&(intersectsLeft || intersectsRight))return CollisionState.BOTH;
                if ((intersectsBottom || intersectsTop)) return CollisionState.VERTICAL;
                if ((intersectsLeft || intersectsRight)) return CollisionState.HORIZONTAL;
            }
            return CollisionState.FALSE;
        }

        public static boolean isImageViewCenterNearLine(double centerX,double centerY,double radius, double x1, double y1, double x2, double y2) {
            // 计算线段到圆心的距离
            double distance = pointToLineDistance(centerX, centerY, x1, y1, x2, y2);

            // 如果距离小于或等于半径，则视为重合
            return distance <= radius;
        }

        private static double pointToLineDistance(double px, double py, double x1, double y1, double x2, double y2) {
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
            return Math.sqrt(dx * dx + dy * dy);
        }
    }


    // 按情况覆盖
    public abstract Bounds getBounds();


    }
