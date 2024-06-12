package com.mambastu.material.pojo.entity.bullet;

import com.mambastu.material.pojo.Interface.Movable;
import com.mambastu.material.pojo.entity.BaseEntity;
import com.mambastu.material.pojo.enums.CollisionState;
import javafx.beans.property.SimpleDoubleProperty;

public abstract class BaseBullet extends BaseEntity implements Movable{

    @Override
    public Bounds getBounds() {//返回圆形类
        double radius = Math.max(showingImageView.getFitWidth(), showingImageView.getFitHeight()) / 2;
        return new CircleBounds(x, y, radius,prevX,prevY);
    }

    @Override
    public void crossedBoundary() {
        double sceneWidth = root.getWidth();
        double sceneHeight = root.getHeight();
        RectangleOutBounds sceneBounds = new RectangleOutBounds(new SimpleDoubleProperty(0.0),new SimpleDoubleProperty(0.0),sceneWidth,sceneHeight);
        CollisionState collisionState = sceneBounds.collisionState(this.getBounds());
        if(collisionState== CollisionState.HORIZONTAL)x=new SimpleDoubleProperty(prevX);
        if(collisionState==CollisionState.VERTICAL)y=new SimpleDoubleProperty(prevY);
        if(collisionState==CollisionState.BOTH){
            x=new SimpleDoubleProperty(prevX);
            y=new SimpleDoubleProperty(prevY);
        }
    }

    public void savePreviousFrame(){
        prevX = x.get();
        prevY = y.get();
    }

}