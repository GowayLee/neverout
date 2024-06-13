package com.mambastu.material.pojo.entity.player;

import java.util.Set;

import com.mambastu.controller.input.comp.GameInput;
import com.mambastu.material.pojo.Interface.Movable;
import com.mambastu.material.pojo.entity.BaseEntity;

import com.mambastu.material.pojo.enums.CollisionState;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BasePlayer extends BaseEntity implements Movable {
    protected double speed = 5;
    protected final SimpleIntegerProperty MaxHP = new SimpleIntegerProperty(100);
    protected final SimpleIntegerProperty HP = new SimpleIntegerProperty(100);

    public enum State {MOVING,SKILL}
    public enum SkillState {READY, ACTIVE, COOLDOWN}
    public enum InjuryState {NORMAL,INVINCIBLE}
    private State state;
    private SkillState skillState;
    private InjuryState injuryState;


    abstract public void move(Set<GameInput> activeInputs);

    public void setPos(double sceneWidth, double sceneHeight) { // 默认出生在屏幕中央
        this.x.set(sceneWidth / 2);
        this.y.set(sceneHeight / 2);
        showingImageView.setX(x.get());
        showingImageView.setY(y.get());
        showingImageView.setFitWidth(50);
        showingImageView.setFitHeight(50);
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
        double radius = Math.max(showingImageView.getFitWidth(), showingImageView.getFitHeight()) / 2;
        return new CircleBounds(x, y, radius, prevX, prevY);
    }

    @Override
    public void crossedBoundary() {
        double sceneWidth = root.getWidth();
        double sceneHeight = root.getHeight();
        RectangleOutBounds sceneBounds = new RectangleOutBounds(new SimpleDoubleProperty(0.0), new SimpleDoubleProperty(0.0), sceneWidth, sceneHeight);
        CollisionState collisionState = sceneBounds.collisionState(this.getBounds());
        if (collisionState == CollisionState.HORIZONTAL) x = new SimpleDoubleProperty(prevX);
        if (collisionState == CollisionState.VERTICAL) y = new SimpleDoubleProperty(prevY);
        if (collisionState == CollisionState.BOTH) {
            x = new SimpleDoubleProperty(prevX);
            y = new SimpleDoubleProperty(prevY);
        }
    }

    public void savePreviousFrame() {
        prevX = x.get();
        prevY = y.get();
    }

    public State getState() {return state;}

    public void setState(State state) {this.state = state;}

    public SkillState getSkillState() {return skillState;}

    public void setSkillState(SkillState skillState) {this.skillState = skillState;}

    public InjuryState getInjuryState() {return injuryState;}

    public void setInjuryState(InjuryState injuryState) {this.injuryState = injuryState;}
}
