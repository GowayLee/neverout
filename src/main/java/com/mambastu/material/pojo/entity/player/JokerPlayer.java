package com.mambastu.material.pojo.entity.player;

import com.mambastu.controller.input.comp.GameInput;
import com.mambastu.material.resource.ResourceManager;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;
import java.util.Set;

@Getter
@Setter
public class JokerPlayer extends BasePlayer {
    private Image bornImage;
    private Image readyImage;
    private Image dieImage;
    private double skillCD = 3; // second
    private Random random = new Random();
    public enum JokerSkillState { RED , BLUE };
    JokerSkillState jokerSkillState;

    public JokerPlayer() {
        this.bornImage = ResourceManager.getInstance().getImg("bornImage", "Player", "Joker");
        this.readyImage = ResourceManager.getInstance().getImg("readyImage", "Player", "Joker");
        this.dieImage = ResourceManager.getInstance().getImg("dieImage", "Player", "Joker");
    }

    @Override
    public void init() { // 初始化为移动状态，技能预备，可受伤
        setState(State.MOVING);
        setSkillState(SkillState.READY);
        setInjuryState(InjuryState.NORMAL);
        showingImage.set(bornImage);
        showingImageView.imageProperty().bind(showingImage);
    }

    @Override
    public void move(Set<GameInput> activeInputs) {
        double deltaX = 0, deltaY = 0;
        savePreviousFrame();
        if (getState() == State.MOVING||getState()==State.SKILL) {
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
            deltaX /= Math.sqrt(2);
            deltaY /= Math.sqrt(2);
        }

        if (getState() == State.SKILL) {
            createDashTrail();
        }

        x.set(x.get() + deltaX);
        y.set(y.get() + deltaY);

        showingImageView.setX(x.get());
        showingImageView.setY(y.get());
        crossedBoundary();
    }

    private void activateSkill(Set<GameInput> activeInputs) { // 技能：随机进入一种状态
        setState(State.SKILL);
        setSkillState(SkillState.ACTIVE);

        int skillType = random.nextInt(2);
        if (skillType == 0) { // 状态1：速度减慢至原速度的0.75倍，不会受到伤害，背后有红色虚影
            setJokerSkillState(JokerSkillState.RED);
            setInjuryState(InjuryState.INVINCIBLE);
            speed *= 0.5;
        } else { // 状态2：速度加快至2倍，会受到伤害，背后有蓝色虚影
            setJokerSkillState(JokerSkillState.BLUE);
            setInjuryState(InjuryState.NORMAL);
            speed *= 2;
        }

        PauseTransition skillTimeline = new PauseTransition(Duration.seconds(2));
        skillTimeline.setOnFinished(event -> {
            setState(State.MOVING);
            setSkillState(SkillState.COOLDOWN);
            setInjuryState(InjuryState.NORMAL);

            // 重置速度为原始值
            if (this.jokerSkillState == JokerSkillState.BLUE) {
                speed /= 2;
            } else if (this.jokerSkillState == JokerSkillState.RED) {
                speed /= 0.5;
            }

            startSkillCooldown();
        });
        skillTimeline.play();
    }


    private void startSkillCooldown() { // 进入技能冷却
        skillCDTimer.setDuration(Duration.seconds(skillCD));
        skillCDTimer.setOnFinished(event -> setSkillState(SkillState.READY));
        skillCDTimer.play();
    }

    public void die() {
        showingImage.set(dieImage);
    }

    @Override
    public void setSkillState(SkillState state) {
        super.setSkillState(state);
        setStateImage();
    }

    public void setStateImage() { // 根据技能的状态来设置图像
        ColorAdjust colorAdjust = new ColorAdjust();
        if (getSkillState() == SkillState.ACTIVE) {
            if (this.jokerSkillState==JokerSkillState.RED) {
                colorAdjust.setHue(-0.5); // 红色
                colorAdjust.setContrast(0.5);
            } else if (this.jokerSkillState==JokerSkillState.BLUE) {
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

    private void createDashTrail() { // 冲刺生成虚影
        // 根据玩家位置生成虚影
        ImageView trail = new ImageView(showingImageView.getImage());
        trail.setFitWidth(showingImageView.getFitWidth());
        trail.setFitHeight(showingImageView.getFitHeight());
        trail.setX(showingImageView.getX());
        trail.setY(showingImageView.getY());

        // 虚影特性：根据技能状态设置颜色，0.5透明度，淡出（时间0.5秒）
        ColorAdjust colorAdjust = new ColorAdjust();
        if (this.jokerSkillState==JokerSkillState.RED) {
            colorAdjust.setHue(0); // 红色
        } else if (this.jokerSkillState==JokerSkillState.BLUE) {
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
