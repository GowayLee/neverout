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

public class BasakerPlayer extends BasePlayer {
    private Image bornImage;
    private Image readyImage;
    private Image dieImage;
    private Image rageImage;
    private Image bloodImage;
    private Random random = new Random();
    private ImageView bloodImageView;

    public enum BasakerSkillState {
        NORMAL, ENRAGED
    };

    private BasakerSkillState basakerSkillState;

    public BasakerPlayer() {
        super();
        super.skillCD.set(5);
        this.bornImage = ResourceManager.getInstance().getImg("bornImage", "Player", "Basaker");
        this.readyImage = ResourceManager.getInstance().getImg("readyImage", "Player", "Basaker");
        this.dieImage = ResourceManager.getInstance().getImg("dieImage", "Player", "Basaker");
        this.rageImage = ResourceManager.getInstance().getImg("enragedImage", "Player", "Basaker");
        this.bloodImage = ResourceManager.getInstance().getImg("bloodImage", "Player", "Basaker");
        setImageSize(50, 50);
        this.basakerSkillState = BasakerSkillState.NORMAL;
        this.invincibleTimer.setCycleCount(8); // 无敌帧循环8次
        this.invincibleTimer.setOnFinished(e -> {
        super.injuryState = InjuryState.NORMAL;
        });

        // 初始化血图层
        bloodImageView = new ImageView(bloodImage);
        bloodImageView.setFitWidth(1920);
        bloodImageView.setFitHeight(1080);
        bloodImageView.setOpacity(0.5);
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
                activateSkill(root);
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

    private void activateSkill(Pane root) { // 技能：进入特殊状态
        state = State.SKILL;
        skillState = SkillState.ACTIVE;
        basakerSkillState = BasakerSkillState.ENRAGED;
        setStateImage();

        // 产生一个巨大的原地50%透明度虚影
        createInitialSkillShadow(root);
        root.getChildren().add(bloodImageView); // 添加血图层到顶层

        PauseTransition skillTimeline = new PauseTransition(Duration.seconds(5));
        skillTimeline.setOnFinished(event -> {
            state = State.MOVING;
            skillState = SkillState.COOLDOWN;
            basakerSkillState = BasakerSkillState.NORMAL;
            setStateImage();
            root.getChildren().remove(bloodImageView); // 移除血图层
            startSkillCooldown();
        });
        skillTimeline.play();
    }

    private void createInitialSkillShadow(Pane root) {
        ImageView skillShadow = new ImageView(rageImage);
        skillShadow.setFitWidth(showingImageView.getFitWidth() * 5);
        skillShadow.setFitHeight(showingImageView.getFitHeight() * 5);
        skillShadow.setX(showingImageView.getX() - showingImageView.getFitWidth() / 2);
        skillShadow.setY(showingImageView.getY() - showingImageView.getFitHeight() / 2);

        skillShadow.setOpacity(0.5);

        FadeTransition fade = new FadeTransition(Duration.seconds(1), skillShadow);
        fade.setFromValue(0.5);
        fade.setToValue(0);
        fade.setOnFinished(event -> root.getChildren().remove(skillShadow));
        fade.play();

        root.getChildren().add(skillShadow);
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
            int finalDamage = damage;
            if (basakerSkillState == BasakerSkillState.ENRAGED) {
                finalDamage *= 2; // 受到的伤害翻倍
            }
            HP.set(HP.get() - finalDamage); // 受到伤害，扣除生命值
            injuryState = InjuryState.INVINCIBLE; // 进入无敌状态
            invincibleTimer.playFromStart();
        }
    }

    public void setStateImage() { // 根据技能的状态来设置图像
        if (basakerSkillState == BasakerSkillState.ENRAGED) {
            showingImage.set(rageImage);
        } else {
            showingImage.set(bornImage);
        }
        showingImageView.imageProperty().bind(showingImage);
    }

    private void createDashTrail(Pane root) { // 冲刺生成虚影
        // 根据玩家位置生成虚影
        ImageView trail = new ImageView(showingImageView.getImage());
        trail.setFitWidth(showingImageView.getFitWidth());
        trail.setFitHeight(showingImageView.getFitHeight());
        trail.setX(showingImageView.getX());
        trail.setY(showingImageView.getY());

        // 虚影特性：火红色，0.5透明度，淡出（时间0.5秒）
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setHue(-0.1); // 火红色
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
