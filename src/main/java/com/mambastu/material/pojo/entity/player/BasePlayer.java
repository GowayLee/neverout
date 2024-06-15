package com.mambastu.material.pojo.entity.player;

import java.util.Set;

import com.mambastu.controller.input.comp.GameInput;
import com.mambastu.material.pojo.Interface.Movable;
import com.mambastu.material.pojo.entity.BaseEntity;
import com.mambastu.material.pojo.weapon.BaseWeapon;

import com.mambastu.material.pojo.enums.CollisionState;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BasePlayer extends BaseEntity implements Movable {
    protected double speed = 5;
    protected final SimpleIntegerProperty MaxHP = new SimpleIntegerProperty(100);
    protected final SimpleIntegerProperty HP = new SimpleIntegerProperty(100);
    protected final PauseTransition skillCDTimer = new PauseTransition(); // 技能冷却时间计时器
    protected final Timeline invincibleTimer = new Timeline( // 无敌帧效果
            new KeyFrame(Duration.ZERO, e -> {
                showingImageView.setOpacity(1.0);
            }),
            new KeyFrame(Duration.millis(25), e -> {
                showingImageView.setOpacity(0.0);
            }),
            new KeyFrame(Duration.millis(50), e -> {
                showingImageView.setOpacity(1.0);
            }));

    protected BaseWeapon weapon;

    protected enum State {
        MOVING, SKILL
    }

    protected enum SkillState {
        READY, ACTIVE, COOLDOWN
    }

    protected enum InjuryState {
        NORMAL, INVINCIBLE
    }

    private State state;
    private SkillState skillState;
    private InjuryState injuryState;

    public void move(Set<GameInput> activeInputs, Pane root) { // TODO: 边界问题
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

        showingImageView.setX(x.get());
        showingImageView.setY(y.get());
        crossedBoundary(root);
    }

    public void setPos(double sceneWidth, double sceneHeight) { // 默认出生在屏幕中央
        this.x.set(sceneWidth / 2);
        this.y.set(sceneHeight / 2);
        showingImageView.setX(x.get());
        showingImageView.setY(y.get());
        setImageSize(50, 50);
    }

    public void getHurt(Integer damage) {
        HP.set(HP.get() - damage); // 受到伤害，扣除生命值
    }

    public boolean isDie() { // 判断玩家是否死亡
        if (HP.get() <= 0) {
            return true;
        }
        return false;
    }

    abstract public void die(); // 设置玩家死亡状态

    @Override
    public Bounds getBounds() {// 返回圆形bound类
        double radius = Math.max(showingImageView.getFitWidth(), showingImageView.getFitHeight()) / 2;
        return new CircleBounds(x, y, radius, prevX, prevY);
    }

    @Override
    public void crossedBoundary(Pane root) {
        double sceneWidth = root.getWidth();
        double sceneHeight = root.getHeight();
        RectangleOutBounds sceneBounds = new RectangleOutBounds(new SimpleDoubleProperty(0.0),
                new SimpleDoubleProperty(0.0), sceneWidth, sceneHeight);
        CollisionState collisionState = sceneBounds.collisionState(this.getBounds());
        if (collisionState == CollisionState.HORIZONTAL)
            x = new SimpleDoubleProperty(prevX);
        if (collisionState == CollisionState.VERTICAL)
            y = new SimpleDoubleProperty(prevY);
        if (collisionState == CollisionState.BOTH) {
            x = new SimpleDoubleProperty(prevX);
            y = new SimpleDoubleProperty(prevY);
        }
    }

    @Override
    public void savePreviousFrame() {
        prevX = x.get();
        prevY = y.get();
    }
}
