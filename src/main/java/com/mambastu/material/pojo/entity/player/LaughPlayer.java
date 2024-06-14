package com.mambastu.material.pojo.entity.player;

import com.mambastu.controller.input.comp.GameInput;
import com.mambastu.material.resource.ResourceManager;

import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class LaughPlayer extends BasePlayer {
    private Image bornImage;
    private Image dieImage;
    private double skillCD = 2;
    private double skillDeltaX;
    private double skillDeltaY;


    public LaughPlayer() {
        this.bornImage = ResourceManager.getInstance().getImg("bornImage", "Player", "Player1");
        this.dieImage = ResourceManager.getInstance().getImg("dieImage", "Player", "Player1");
        this.invincibleTimer.setCycleCount(8); // 无敌帧循环8次
        this.invincibleTimer.setOnFinished(e -> {
            setInjuryState(InjuryState.NORMAL);
        });
    }

    @Override
    public void init() { // 初始化方法，设置初始状态和图片等属性
        setState(State.MOVING);
        setSkillState(SkillState.READY);
        setInjuryState(InjuryState.NORMAL);
        skillDeltaX = 0;
        skillDeltaY = 0;
        showingImage.set(bornImage);
        showingImageView.imageProperty().bind(showingImage);
    }

    @Override
    public void move(Set<GameInput> activeInputs, Pane root) {
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

        x.set(x.get() + deltaX);
        y.set(y.get() + deltaY);

        showingImageView.setX(x.get());
        showingImageView.setY(y.get());
        
        crossedBoundary(root);
    }

    @Override
    public void getHurt(Integer damage) {
        if (injuryState != InjuryState.INVINCIBLE && damage > 0) {
            HP.set(HP.get() - damage); // 受到伤害，扣除生命值
            setInjuryState(InjuryState.INVINCIBLE); // 进入无敌状态
            invincibleTimer.playFromStart();
        }
    }

    private void activateSkill(Set<GameInput> activeInputs) {
        setState(State.SKILL);
        setSkillState(SkillState.ACTIVE);
        setInjuryState(InjuryState.INVINCIBLE); // 无敌状态，不受伤害
        skillDeltaX = 0;
        skillDeltaY = 0;

        if (activeInputs.contains(GameInput.MOVE_UP))
            skillDeltaY -= speed * 10;
        if (activeInputs.contains(GameInput.MOVE_DOWN))
            skillDeltaY += speed * 10;
        if (activeInputs.contains(GameInput.MOVE_LEFT))
            skillDeltaX -= speed * 10;
        if (activeInputs.contains(GameInput.MOVE_RIGHT))
            skillDeltaX += speed * 10;

        PauseTransition skillTimeline = new PauseTransition(Duration.seconds(0.05));
        skillTimeline.setOnFinished(event -> {
            setState(State.MOVING);
            setSkillState(SkillState.COOLDOWN);
            setInjuryState(InjuryState.NORMAL);
            startSkillCooldown();
        });
        skillTimeline.play();
    }

    private void startSkillCooldown() {
        skillCDTimer.setDuration(Duration.seconds(skillCD));
        skillCDTimer.setOnFinished(event -> setSkillState(SkillState.READY));
        skillCDTimer.play();
    }

    public void die() {
        showingImage.set(dieImage);
    }

}
