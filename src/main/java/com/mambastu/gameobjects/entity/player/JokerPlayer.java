package com.mambastu.gameobjects.entity.player;

import com.mambastu.enums.GameInput;
import com.mambastu.resource.media.impl.AudioManager;
import com.mambastu.resource.media.impl.ImageManager;
import com.mambastu.utils.BetterMath;
import com.mambastu.utils.GlobalVar;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.Random;
import java.util.Set;

public class JokerPlayer extends BasePlayer {
    private Image bornImage;
    private Image dieImage;
    private Random random = new Random();
    private double damageRatio;

    private final PauseTransition skillTimeline;
    private final ColorAdjust skillColorAdjust;

    public enum JokerSkillState {
        RED, BLUE
    };

    private JokerSkillState jokerSkillState;

    public JokerPlayer() {
        super();
        super.skillCD.set(2);
        this.bornImage = ImageManager.getInstance().getImg("bornImage", "Player", "JokerPlayer");
        this.dieImage = ImageManager.getInstance().getImg("dieImage", "Player", "JokerPlayer");
        setImageSize(50, 50);
        super.skillCD.set(5.0);
        this.damageRatio = 1.0;
        this.invincibleTimer.setCycleCount(8); // 无敌帧循环8次
        this.invincibleTimer.setOnFinished(e -> {
            super.injuryState = InjuryState.NORMAL;
        });
        this.skillTimeline = new PauseTransition();
        this.skillColorAdjust = new ColorAdjust();
        initSkillFX();
    }

    private void initSkillFX() {
        skillTimeline.setDuration(Duration.seconds(2));
        skillTimeline.setOnFinished(event -> {
            state = State.MOVING;
            skillState = SkillState.COOLDOWN;
            injuryState = InjuryState.NORMAL;
            if (jokerSkillState == JokerSkillState.BLUE) {
                damageRatio = 0.3;
                speed.set(speed.get() / 2);
            } else if (jokerSkillState == JokerSkillState.RED) {
                speed.set(speed.get() * 2);
            }
            startSkillCooldown();
        });

        skillColorAdjust.setContrast(0.5);
        skillColorAdjust.setSaturation(0.5);
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
    public void move(Set<GameInput> activeInputs) {
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
            createDashTrail();
        }
        x.set(x.get() + deltaX);
        y.set(y.get() + deltaY);
        showingImageView.setX(x.get());
        showingImageView.setY(y.get());
        trappedInStage();
    }

    private void activateSkill(Set<GameInput> activeInputs) { // 技能：随机进入一种状态
        jokerSkillState = random.nextInt(2) == 0 ? JokerSkillState.RED : JokerSkillState.BLUE;
        state = State.SKILL;
        skillState = SkillState.ACTIVE;

        if (jokerSkillState == JokerSkillState.RED) {
            AudioManager.getInstance().playAudio("SoundEffects", "SkillJokerDown", "displayAudio");
            injuryState = InjuryState.INVINCIBLE;
            speed.set(this.speed.get() / 2);
        } else {
            damageRatio = 0.3;
            injuryState = InjuryState.NORMAL;
            speed.set(this.speed.get() * 2);
            AudioManager.getInstance().playAudio("SoundEffects", "SkillJokerUp", "displayAudio");
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

    private void createDashTrail() { // 冲刺生成虚影
        // 根据玩家位置生成虚影
        ImageView trail = new ImageView(showingImageView.getImage());
        trail.setFitWidth(showingImageView.getFitWidth());
        trail.setFitHeight(showingImageView.getFitHeight());
        trail.setX(showingImageView.getX());
        trail.setY(showingImageView.getY());

        // 虚影特性：根据技能状态设置颜色，0.5透明度，淡出（时间0.5秒）;
        skillColorAdjust.setHue(jokerSkillState == JokerSkillState.RED ? -0.05 : -0.9);

        trail.setEffect(skillColorAdjust);
        trail.setOpacity(0.5);
        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), trail);
        fade.setFromValue(0.5);
        fade.setToValue(0);
        fade.setOnFinished(event -> GlobalVar.getGamePane().getChildren().remove(trail));
        fade.play();

        GlobalVar.getGamePane().getChildren().add(trail);
    }

    public void die() {
        showingImage.set(dieImage);
    }
}
