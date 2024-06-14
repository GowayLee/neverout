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
    private double skillDeltaX;
    private double skillDeltaY;
    private Random random = new Random();

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
        skillDeltaX = 0;
        skillDeltaY = 0;
        showingImage.set(bornImage);
        showingImageView.imageProperty().bind(showingImage);
    }

    @Override
    public void move(Set<GameInput> activeInputs) {
        double deltaX = 0, deltaY = 0;
        savePreviousFrame();
        if (getState() == State.MOVING) {
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
        } else if (getState() == State.SKILL) {
            deltaX = skillDeltaX;
            deltaY = skillDeltaY;
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

        skillDeltaX = 0;
        skillDeltaY = 0;

        boolean moveUp = activeInputs.contains(GameInput.MOVE_UP);
        boolean moveDown = activeInputs.contains(GameInput.MOVE_DOWN);
        boolean moveLeft = activeInputs.contains(GameInput.MOVE_LEFT);
        boolean moveRight = activeInputs.contains(GameInput.MOVE_RIGHT);

        if (moveUp)
            skillDeltaY -= speed;
        if (moveDown)
            skillDeltaY += speed;
        if (moveLeft)
            skillDeltaX -= speed;
        if (moveRight)
            skillDeltaX += speed;

        if (moveUp && moveLeft || moveUp && moveRight || moveDown && moveLeft || moveDown && moveRight) {
            double scale = 1 / Math.sqrt(2);
            skillDeltaX *= scale;
            skillDeltaY *= scale;
        }

        int skillType = random.nextInt(2);
        if (skillType == 0) {
            // 状态1：速度减慢至原速度的0.75倍，不会受到伤害，背后有红色虚影
            speed *= 0.75;
            setInjuryState(InjuryState.INVINCIBLE);
        } else {
            // 状态2：速度加快至2倍，会受到伤害，背后有蓝色虚影
            speed *= 2;
            setInjuryState(InjuryState.NORMAL);
        }

        PauseTransition skillTimeline = new PauseTransition(Duration.seconds(3));
        skillTimeline.setOnFinished(event -> {
            setState(State.MOVING);
            setSkillState(SkillState.COOLDOWN);
            setInjuryState(InjuryState.NORMAL);
            speed = 5; // 重置速度为原始值
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
            if (speed < 5) {
                colorAdjust.setHue(0); // 红色
            } else if (speed > 5) {
                colorAdjust.setHue(0.5); // 蓝色
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
        if (speed < 5) {
            colorAdjust.setHue(0); // 红色
        } else if (speed > 5) {
            colorAdjust.setHue(0.5); // 蓝色
        }
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
