package com.mambastu.material.pojo.entity.monster;

import java.util.Random;

import com.mambastu.material.pojo.Interface.Movable;
import com.mambastu.material.pojo.entity.BaseEntity;
import com.mambastu.material.pojo.entity.player.BasePlayer;
import com.mambastu.material.pojo.enums.CollisionState;
import javafx.beans.property.SimpleDoubleProperty;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public abstract class BaseMonster extends BaseEntity implements Movable  {
    protected final SimpleIntegerProperty HP = new SimpleIntegerProperty(); // 怪物血量
    protected double speed;

    abstract public void move(double targX, double targY); // 怪物的移动需要参照目标
    public enum State{OMEN,IDLE,MOVING,SHAKING};
    private State state;

    abstract public void die(Pane root);

    public void getHurt(Integer damage) {
        HP.set(HP.get() - damage); // 怪物受到伤害

        colorAdjust.setBrightness(0.9); // 将亮度设置为最大，变成白色
        showingImageView.setEffect(colorAdjust);

        FXTimer.setDuration(Duration.millis(50));
        FXTimer.setOnFinished(event -> showingImageView.setEffect(null));
        FXTimer.play();
    }

    public boolean isDie() { // 判断是否死亡
        return HP.get() <= 0;
    }

    public void setPos(double sceneWidth, double sceneHeight, BasePlayer player) { // 默认在地图中随机生成 TODO: 避开玩家
        Random rand = new Random();
        // 怪物避开玩家的半径
        double avoidRadius = 50;
        do {
            x.set(rand.nextDouble() * sceneWidth);
            y.set(rand.nextDouble() * sceneHeight);
        } while (isInAvoidRange(player.getX().get(), player.getY().get(), avoidRadius));
        showingImageView.setX(x.get());
        showingImageView.setY(y.get());
        setImageSize(50, 50);
    }

    private boolean isInAvoidRange(double playerX, double playerY, double avoidRadius) {
        double dx = x.get() - playerX;
        double dy = y.get() - playerY;
        double distanceSquared = dx * dx + dy * dy;
        return distanceSquared < avoidRadius * avoidRadius;
    }

    @Override
    public Bounds getBounds() {//返回圆形bound类
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

    public State getState() {return state;}
    public void setState(State state) {this.state = state;}
}
