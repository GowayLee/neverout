package com.mambastu.gameobjects.entity.player;

import java.util.Set;

import com.mambastu.enums.GameInput;
import com.mambastu.gameobjects.bound.CircleBound;
import com.mambastu.gameobjects.entity.BaseEntity;
import com.mambastu.gameobjects.weapon.BaseWeapon;
import com.mambastu.utils.BetterMath;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class BasePlayer extends BaseEntity{
    protected final SimpleDoubleProperty speed = new SimpleDoubleProperty(5.0);
    protected final SimpleDoubleProperty skillCD = new SimpleDoubleProperty(); // 秒
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
    @Setter
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

    protected State state;
    protected SkillState skillState;
    protected InjuryState injuryState;

    public BasePlayer() { // 默认构造函数内设置玩家的碰撞箱
        this.bound = new CircleBound(x, y, 50, prevX, prevY);
        this.skillCD.set(2.0); // 默认技能冷却时间2秒
        this.skillCDTimer.setDuration(Duration.seconds(skillCD.get()));
        this.skillCDTimer.setOnFinished(event -> skillState = SkillState.READY);
        this.invincibleTimer.setCycleCount(8); // 无敌帧循环8次
        this.invincibleTimer.setOnFinished(e -> {
            injuryState = InjuryState.NORMAL;
        });
    }

    public void move(Set<GameInput> activeInputs) {
        double deltaX = 0, deltaY = 0;
        double speed = this.speed.get(); // 获取玩家速度属性值
        if (activeInputs.contains(GameInput.MOVE_UP))
            deltaY -= speed;
        if (activeInputs.contains(GameInput.MOVE_DOWN))
            deltaY += speed;
        if (activeInputs.contains(GameInput.MOVE_LEFT))
            deltaX -= speed;
        if (activeInputs.contains(GameInput.MOVE_RIGHT))
            deltaX += speed;

        if (deltaX != 0 && deltaY != 0) {
            deltaX /= BetterMath.sqrt(2);
            deltaY /= BetterMath.sqrt(2);
        }

        x.set(x.get() + deltaX);
        y.set(y.get() + deltaY);

        showingImageView.setX(x.get());
        showingImageView.setY(y.get());
        trappedInStage();
    }

    public void setPos(double sceneWidth, double sceneHeight) { // 默认出生在屏幕中央
        this.x.set(sceneWidth / 2);
        this.y.set(sceneHeight / 2);
        showingImageView.setX(x.get());
        showingImageView.setY(y.get());
    }

    protected void startSkillCooldown () {
        skillState = SkillState.COOLDOWN;
        skillCDTimer.play();
    }

    public void getHurt(Integer damage) {
        if (this.getInjuryState() != InjuryState.INVINCIBLE) {
            HP.set(HP.get() - damage); // 受到伤害，扣除生命值
            injuryState = InjuryState.INVINCIBLE; // 进入无敌帧
            invincibleTimer.playFromStart();
        }
    }

    public boolean isDie() { // 判断玩家是否死亡
        if (HP.get() <= 0) {
            return true;
        }
        return false;
    }

    abstract public void die(); // 设置玩家死亡状态
}
