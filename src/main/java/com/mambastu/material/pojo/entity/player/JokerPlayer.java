package com.mambastu.material.pojo.entity.player;

import com.mambastu.controller.input.comp.GameInput;
import com.mambastu.material.resource.ResourceManager;
import com.mambastu.util.AudioManager;
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
    private Image dieImage;
    private Random random = new Random();
    private double damageRatio;

    private final PauseTransition skillTimeline = new PauseTransition();

    public enum JokerSkillState {
        RED, BLUE
    };

    private JokerSkillState jokerSkillState;

    public JokerPlayer() {
        super();
        super.skillCD.set(2);
        this.bornImage = ResourceManager.getInstance().getImg("bornImage", "Player", "JokerPlayer");
        this.dieImage = ResourceManager.getInstance().getImg("dieImage", "Player", "JokerPlayer");
        setImageSize(50, 50);
        this.damageRatio = 1.0;
        this.jokerSkillState = JokerSkillState.BLUE;
        this.invincibleTimer.setCycleCount(8); // 无敌帧循环8次
        this.invincibleTimer.setOnFinished(e -> {
            super.injuryState = InjuryState.NORMAL;
        });
        this.skillTimeline.setDuration(Duration.seconds(2));
        this.skillTimeline.setOnFinished(event -> {
            state = State.MOVING;
            skillState = SkillState.COOLDOWN;
            injuryState = InjuryState.NORMAL;
            if (this.jokerSkillState == JokerSkillState.BLUE) {
                damageRatio = 1.0;
                speed.set(this.speed.get() / 2);
            } else if (this.jokerSkillState == JokerSkillState.RED) {
                speed.set(this.speed.get() * 2);
            }
            startSkillCooldown();
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
            skillSoundEffects();
            createDashTrail(root);
        }
        x.set(x.get() + deltaX);
        y.set(y.get() + deltaY);
        showingImageView.setX(x.get());
        showingImageView.setY(y.get());
        trappedInStage();
    }

    private void skillSoundEffects() {
        if(this.jokerSkillState==JokerSkillState.RED){
            if(!AudioManager.getInstance().isAudioPlaying("SoundEffects", "SkillJokerDown","displayAudio")){
                AudioManager.getInstance().playAudio("SoundEffects", "SkillJokerDown","displayAudio");
            }
        }else if(this.jokerSkillState==JokerSkillState.BLUE){
            if(!AudioManager.getInstance().isAudioPlaying("SoundEffects", "SkillJokerUp","displayAudio")){
                AudioManager.getInstance().playAudio("SoundEffects", "SkillJokerUp","displayAudio");
            }
        }
    }

    private void activateSkill(Set<GameInput> activeInputs) { // 技能：随机进入一种状态
        state = State.SKILL;
        skillState = SkillState.ACTIVE;
        
        int skillType = random.nextInt(2);
        if (skillType == 0) { // 状态1：速度减慢至原速度的0.75倍，不会受到伤害，背后有红色虚影
            jokerSkillState = JokerSkillState.RED;
            injuryState = InjuryState.INVINCIBLE;
            speed.set(this.speed.get() / 2);
        } else {
            damageRatio = 0.3;
            jokerSkillState = JokerSkillState.BLUE;
            injuryState = InjuryState.NORMAL;
            speed.set(this.speed.get() * 2);
        }
        skillTimeline.play();
    }

    @Override
    public void getHurt(Integer damage) {
        if (super.getInjuryState() != InjuryState.INVINCIBLE) {
            HP.set((int) (HP.get() - damage * damageRatio)); // 加入免伤比例计算
            injuryState = InjuryState.INVINCIBLE; // 进入无敌状态
            invincibleTimer.playFromStart();
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

    public void die() {
        showingImage.set(dieImage);
    }
}
