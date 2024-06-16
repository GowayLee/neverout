package com.mambastu.material.pojo.entity.monster;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import com.mambastu.material.pojo.bound.CircleBound;
import com.mambastu.material.pojo.entity.BaseEntity;
import com.mambastu.material.pojo.entity.bullet.BaseBullet;
import com.mambastu.material.pojo.entity.player.BasePlayer;

import javafx.animation.PauseTransition;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import lombok.Getter;

public abstract class BaseMonster extends BaseEntity {
    protected final SimpleIntegerProperty HP = new SimpleIntegerProperty(); // 怪物血量
    protected double speed;
    protected int damage;
    protected State state;
    @Getter
    protected final PauseTransition initTimer = new PauseTransition();
    protected final ColorAdjust colorAdjust = new ColorAdjust(); // 特效
    protected final PauseTransition hurtFXTimer = new PauseTransition(); // 受伤特效&无敌帧计时器

    protected final Queue<BaseBullet> inBulletQueue = new ArrayDeque<>();
    protected final PauseTransition inBulletQueueTimer = new PauseTransition(); // 无敌子弹队列计时器

    protected enum State {
        OMEN, NORMAL, DIE
    };

    protected enum InjuryState {
        NORMAL, INVINCIBLE
    }

    public BaseMonster() {
        this.bound = new CircleBound(x, y, 50, prevX, prevY);
        this.colorAdjust.setBrightness(0.9); // 将亮度设置为最大，变成白色
        this.hurtFXTimer.setDuration(Duration.millis(50));
        this.hurtFXTimer.setOnFinished(event -> showingImageView.setEffect(null));
        this.inBulletQueueTimer.setDuration(Duration.millis(100));
        this.inBulletQueueTimer.setOnFinished(event -> processQueue());
    }

    abstract public void init();

    abstract public void omen(List<BaseMonster> monsterList);

    abstract public void move(double targX, double targY); // 怪物的移动需要参照目标

    /**
     * 一般写有判断怪物当前阶段是否可以造成伤害
     * 
     * @return Integer 怪物释放的伤害值，用于攻击玩家
     */
    abstract public Integer releaseDamage();

    abstract public void die(Pane root);

    public void getHurt(Integer damage, BaseBullet bullet) {
        if (state == State.NORMAL && !inBulletQueue.contains(bullet)) { // 如果怪物处于正常状态，并且子弹不在无敌子弹队列中，则造成伤害
            HP.set(HP.get() - damage);
            if (HP.get() > 0) { // 如果怪物血量大于0，则播放受伤动画，并进入无敌帧状态
                showingImageView.setEffect(colorAdjust);
                hurtFXTimer.play();
                inBulletQueue.add(bullet); // 将子弹加入无敌子弹队列中，防止连击
                runQueueTimer(); // 尝试开始无敌子弹队列计时器
            }
        }
    }

    private void runQueueTimer() {
        if (inBulletQueueTimer.getStatus() != PauseTransition.Status.RUNNING) { // 如果无敌子弹队列计时器没有在运行，则开始运行
            inBulletQueueTimer.play();
        }
    }

    private void processQueue() {
        if (!inBulletQueue.isEmpty()) {
            inBulletQueue.poll();
            inBulletQueueTimer.play(); // 继续下一个100ms的计时
        }
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
    }

    private boolean isInAvoidRange(double playerX, double playerY, double avoidRadius) {
        double dx = x.get() - playerX;
        double dy = y.get() - playerY;
        double distanceSquared = dx * dx + dy * dy;
        return distanceSquared < avoidRadius * avoidRadius;
    }

}
