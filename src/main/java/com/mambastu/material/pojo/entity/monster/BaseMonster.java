package com.mambastu.material.pojo.entity.monster;

import java.util.List;
import java.util.Random;

import com.mambastu.material.pojo.Interface.Movable;
import com.mambastu.material.pojo.entity.BaseEntity;
import com.mambastu.material.pojo.entity.player.BasePlayer;
import com.mambastu.material.pojo.enums.CollisionState;

import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleDoubleProperty;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseMonster extends BaseEntity implements Movable {
    protected final SimpleIntegerProperty HP = new SimpleIntegerProperty(); // 怪物血量
    protected double speed;
    protected int damage;
    protected State state;
    @Getter
    protected final PauseTransition initTimer = new PauseTransition(); 
    protected enum State {
        OMEN, IDLE, MOVING, SHAKING
    };

    protected final ColorAdjust colorAdjust = new ColorAdjust(); // 特效
    protected final PauseTransition hurtFXTimer = new PauseTransition(); // 受伤特效计时器

    abstract public void init();

    abstract public void omen(List<BaseMonster> monsterList);

    abstract public void move(double targX, double targY, Pane root); // 怪物的移动需要参照目标

    /**
     * 一般写有判断怪物当前阶段是否可以造成伤害
     * @return Integer 怪物释放的伤害值，用于攻击玩家
     */
    abstract public Integer releaseDamage();

    abstract public void die(Pane root);

    public void getHurt(Integer damage, Pane root) { 
        HP.set(HP.get() - damage); // 怪物受到伤害

        colorAdjust.setBrightness(0.9); // 将亮度设置为最大，变成白色
        showingImageView.setEffect(colorAdjust);
        hurtFXTimer.setDuration(Duration.millis(50));
        hurtFXTimer.setOnFinished(event -> showingImageView.setEffect(null));
        hurtFXTimer.play();
    }

    public boolean isDie() { // 判断是否死亡
        return HP.get() <= 0;
    }

    public void setPos(double sceneWidth, double sceneHeight, BasePlayer player) {
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

    public void savePreviousFrame() {
        prevX = x.get();
        prevY = y.get();
    }
}
