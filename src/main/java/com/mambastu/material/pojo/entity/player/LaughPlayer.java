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

import java.util.Set;

public class LaughPlayer extends BasePlayer {
    private Image bornImage;
    private Image readyImage;
    private Image dieImage;
    private double skillDeltaX;
    private double skillDeltaY;

    public LaughPlayer() {
        super();
        super.skillCD.set(2);
        this.bornImage = ResourceManager.getInstance().getImg("bornImage", "Player", "Player1");
        this.readyImage = ResourceManager.getInstance().getImg("readyImage", "Player", "Player1");
        this.dieImage = ResourceManager.getInstance().getImg("dieImage", "Player", "Player1");
        setImageSize(50, 50);
        this.invincibleTimer.setCycleCount(8); // 无敌帧循环8次
        this.invincibleTimer.setOnFinished(e -> {
            injuryState = InjuryState.NORMAL;
        });
    }

    @Override
    public void init() { // 初始化为移动状态，技能预备，可受伤
        state = State.MOVING;
        skillState = SkillState.READY;
        injuryState = InjuryState.NORMAL;
        skillDeltaX = 0;
        skillDeltaY = 0;
        showingImage.set(bornImage);
        showingImageView.imageProperty().bind(showingImage);
    }

    @Override
    public void move(Set<GameInput> activeInputs, Pane root) {
        double deltaX = 0, deltaY = 0;
        double speed = this.speed.get();
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
        if(!AudioManager.getInstance().isAudioPlaying("SoundEffects", "SkillLaugh","displayAudio")){
            AudioManager.getInstance().playAudio("SoundEffects", "SkillLaugh","displayAudio");
        }
        }

    @Override
    public void getHurt(Integer damage) {
        if (injuryState != InjuryState.INVINCIBLE) {
            HP.set(HP.get() - damage); // 受到伤害，扣除生命值
            injuryState = InjuryState.INVINCIBLE; // 进入无敌状态
            invincibleTimer.playFromStart();
        }
    }

    private void activateSkill(Set<GameInput> activeInputs) {// 技能：根据玩家的输入向前免伤冲刺
        state = State.SKILL;
        skillState = SkillState.ACTIVE;
        injuryState = InjuryState.INVINCIBLE; // 无敌状态，不受伤害
        setStateImage();

        skillDeltaX = 0;
        skillDeltaY = 0;

        boolean moveUp = activeInputs.contains(GameInput.MOVE_UP);
        boolean moveDown = activeInputs.contains(GameInput.MOVE_DOWN);
        boolean moveLeft = activeInputs.contains(GameInput.MOVE_LEFT);
        boolean moveRight = activeInputs.contains(GameInput.MOVE_RIGHT);
        if (moveUp)
            skillDeltaY -= speed.get() * 7;
        if (moveDown)
            skillDeltaY += speed.get() * 7;
        if (moveLeft)
            skillDeltaX -= speed.get() * 7;
        if (moveRight)
            skillDeltaX += speed.get() * 7;
        // 平衡斜向移动时的距离
        if ((moveUp || moveDown) && (moveLeft || moveRight)) {
            skillDeltaX *= 1 / BetterMath.sqrt(2);
            skillDeltaY *= 1 / BetterMath.sqrt(2);
        }

        PauseTransition skillTimeline = new PauseTransition(Duration.seconds(0.1));
        skillTimeline.setOnFinished(event -> {
            state = State.MOVING;
            skillState = SkillState.COOLDOWN;
            injuryState = InjuryState.NORMAL;
            startSkillCooldown();
        });
        skillTimeline.play();
    }

    private void startSkillCooldown() {// 进入技能冷却
        skillCDTimer.setDuration(Duration.seconds(skillCD.get()));
        skillCDTimer.setOnFinished(event -> skillState = SkillState.READY);
        skillCDTimer.play();
    }

    private void setStateImage() {// 根据技能的状态来设置图像，CD条替代物
        ColorAdjust colorAdjust = new ColorAdjust();
        if (getSkillState() == SkillState.ACTIVE) {
            colorAdjust.setHue(1);
            showingImageView.setEffect(colorAdjust);
        } else if (getSkillState() == SkillState.READY) {
            showingImage.set(readyImage);
            showingImageView.setEffect(colorAdjust);
        } else {
            showingImage.set(bornImage);
            showingImageView.setEffect(null);
        }
    }

    private void createDashTrail(Pane root) {// 冲刺生成虚影
        // 根据玩家位置生成虚影
        ImageView trail = new ImageView(showingImageView.getImage());
        trail.setFitWidth(showingImageView.getFitWidth());
        trail.setFitHeight(showingImageView.getFitHeight());
        trail.setX(showingImageView.getX());
        trail.setY(showingImageView.getY());

        // 虚影特性： 变蓝 0.5透明度 淡出（时间0.5秒）
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setHue(0.5);
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
