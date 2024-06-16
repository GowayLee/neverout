package com.mambastu.material.pojo.entity.player;

import com.mambastu.controller.input.comp.GameInput;
import com.mambastu.material.resource.ResourceManager;
import com.mambastu.util.BetterMath;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.Random;
import java.util.Set;

public class JokerPlayer extends BasePlayer {
    private Image bornImage;
    private Image readyImage;
    private Image dieImage;
    private Random random = new Random();

    public enum JokerSkillState {
        RED, BLUE
    };

    private JokerSkillState jokerSkillState;

    public JokerPlayer() {
        super();
        super.skillCD.set(2);
        this.bornImage = ResourceManager.getInstance().getImg("bornImage", "Player", "Joker");
        this.readyImage = ResourceManager.getInstance().getImg("readyImage", "Player", "Joker");
        this.dieImage = ResourceManager.getInstance().getImg("dieImage", "Player", "Joker");
        setImageSize(50, 50);
        this.jokerSkillState = JokerSkillState.BLUE;
        this.invincibleTimer.setCycleCount(8); // 无敌帧循环8次
        this.invincibleTimer.setOnFinished(e -> {
            super.injuryState = InjuryState.NORMAL;
        });
    }

    @Override
    public void init() { // 初始化为移动状态，技能预备，可受伤
        state = State.MOVING;
        skillState = SkillState.READY;
        injuryState = InjuryState.NORMAL;
        showingImage.set(bornImage);
        showingImageView.imageProperty().bind(showingImage);
    }

    @Override
    public void move(Set<GameInput> activeInputs, Pane root) {
        double deltaX = 0, deltaY = 0;
        double speed = this.speed.get();
        if (getState() == State.MOVING || getState() == State.SKILL) {
            if (activeInputs.contains(GameInput.MOVE_UP))
                deltaY -= speed;
            if (activeInputs.contains(GameInput.MOVE_DOWN))
                deltaY += speed;
            if (activeInputs.contains(GameInput.MOVE_LEFT))
                deltaX -= speed;
            if (activeInputs.contains(GameInput.MOVE_RIGHT))
                deltaX += speed;
            if (activeInputs.contains(GameInput.SKILL) && getSkillState() == SkillState.READY)
                activateSkill(activeInputs);
        }
        if (deltaX != 0 && deltaY != 0 && getState() == State.MOVING) {
            deltaX /= BetterMath.sqrt(2);
            deltaY /= BetterMath.sqrt(2);
        }
        if (getState() == State.SKILL) {
            createDashTrail(root);
        }
        x.set(x.get() + deltaX);
        y.set(y.get() + deltaY);
        showingImageView.setX(x.get());
        showingImageView.setY(y.get());
        trappedInStage();
    }

    private void activateSkill(Set<GameInput> activeInputs) { // 技能：随机进入一种状态
        state = State.SKILL;
        skillState = SkillState.ACTIVE;
        setStateImage();
        
        int skillType = random.nextInt(2);
        if (skillType == 0) { // 状态1：速度减慢至原速度的0.75倍，不会受到伤害，背后有红色虚影
            jokerSkillState = JokerSkillState.RED;
            injuryState = InjuryState.INVINCIBLE;
            speed.set(this.speed.get() / 2);
        } else {
            jokerSkillState = JokerSkillState.BLUE;
            injuryState = InjuryState.NORMAL;
            speed.set(this.speed.get() * 2);
        }

        PauseTransition skillTimeline = new PauseTransition(Duration.seconds(2));
        skillTimeline.setOnFinished(event -> {
            state = State.MOVING;
            skillState = SkillState.COOLDOWN;
            injuryState = InjuryState.NORMAL;
            // 重置速度为原始值
            if (this.jokerSkillState == JokerSkillState.BLUE) {
                speed.set(this.speed.get() / 2);
            } else if (this.jokerSkillState == JokerSkillState.RED) {
                speed.set(this.speed.get() * 2);
            }

            startSkillCooldown();
        });
        skillTimeline.play();
    }

    private void startSkillCooldown() { // 进入技能冷却
        skillCDTimer.setDuration(Duration.seconds(skillCD.get()));
        skillCDTimer.setOnFinished(event -> skillState = SkillState.READY);
        skillCDTimer.play();
    }

    public void die() {
        showingImage.set(dieImage);
    }

    @Override
    public void getHurt(Integer damage) {
        if (super.getInjuryState() != InjuryState.INVINCIBLE) {
            HP.set(HP.get() - damage); // 受到伤害，扣除生命值
            injuryState = InjuryState.INVINCIBLE; // 进入无敌状态
            invincibleTimer.playFromStart();
        }
    }

    public void setStateImage() { // 根据技能的状态来设置图像
        ColorAdjust colorAdjust = new ColorAdjust();
        if (getSkillState() == SkillState.ACTIVE) {
            if (this.jokerSkillState == JokerSkillState.RED) {
                colorAdjust.setHue(-0.5); // 红色
                colorAdjust.setContrast(0.5);
            } else if (this.jokerSkillState == JokerSkillState.BLUE) {
                colorAdjust.setHue(0.5); //
                colorAdjust.setContrast(0.5);
            }
            showingImageView.setEffect(colorAdjust);
        } else if (getSkillState() == SkillState.READY) {
            showingImage.set(readyImage);
            showingImageView.setEffect(null);
        } else {
            showingImage.set(bornImage);
            showingImageView.setEffect(null);
        }
    }

    private void createDashTrail(Pane root) { // 冲刺生成虚影
        // 根据玩家位置生成虚影
        ImageView trail = new ImageView(showingImageView.getImage());
        trail.setFitWidth(showingImageView.getFitWidth());
        trail.setFitHeight(showingImageView.getFitHeight());
        trail.setX(showingImageView.getX());
        trail.setY(showingImageView.getY());

        // 虚影特性：根据技能状态设置颜色，0.5透明度，淡出（时间0.5秒）
        ColorAdjust colorAdjust = new ColorAdjust();
        if (this.jokerSkillState == JokerSkillState.RED) {
            colorAdjust.setHue(0); // 红色
        } else if (this.jokerSkillState == JokerSkillState.BLUE) {
            colorAdjust.setHue(0.5); // 蓝色
        }
        colorAdjust.setContrast(0.5);
        colorAdjust.setSaturation(0.5);
        trail.setEffect(colorAdjust);
        trail.setOpacity(0.5);
        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), trail);
        fade.setFromValue(0.5);
        fade.setToValue(0);
        fade.setOnFinished(event -> root.getChildren().remove(trail));
        fade.play();

        root.getChildren().add(trail);
    }
}
