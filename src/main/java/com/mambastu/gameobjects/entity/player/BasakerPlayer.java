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

import java.util.Set;

public class BasakerPlayer extends BasePlayer {
    private final Image bornImage;
    private final Image dieImage;
    private final Image rageImage;
    private final Image bloodImage;
    private final ImageView bloodImageView;

    private final ImageView skillShadow;

    private final PauseTransition skillTimeline;
    private final FadeTransition shadowFade;
    private final ColorAdjust trailColorAdjust;

    public enum BasakerSkillState {
        NORMAL, ENRAGED
    };

    private BasakerSkillState basakerSkillState;

    public BasakerPlayer() {
        super();
        super.skillCD.set(5);
        this.bornImage = ImageManager.getInstance().getImg("bornImage", "Player", "Basaker");
        this.dieImage = ImageManager.getInstance().getImg("dieImage", "Player", "Basaker");
        this.rageImage = ImageManager.getInstance().getImg("enragedImage", "Player", "Basaker");
        this.bloodImage = ImageManager.getInstance().getImg("bloodImage", "Player", "Basaker");
        setImageSize(50, 50);

        this.invincibleTimer.setCycleCount(8); // 无敌帧循环8次
        this.invincibleTimer.setOnFinished(e -> {
            super.injuryState = InjuryState.NORMAL;
        });

        // 初始化技能特效
        this.skillShadow = new ImageView();
        this.bloodImageView = new ImageView(bloodImage);
        this.skillTimeline = new PauseTransition();
        this.shadowFade = new FadeTransition();
        this.trailColorAdjust = new ColorAdjust();
        initSkillFX();
    }

    private void initSkillFX() {
        skillShadow.setImage(rageImage);
        skillShadow.setFitWidth(showingImageView.getFitWidth() * 8);
        skillShadow.setFitHeight(showingImageView.getFitHeight() * 8);
        skillShadow.setOpacity(0.5);

        bloodImageView.setFitWidth(GlobalVar.getScreenWidth());
        bloodImageView.setFitHeight(GlobalVar.getScreenHeight());
        bloodImageView.setOpacity(0.5);

        skillTimeline.setDuration(Duration.seconds(5.0));
        skillTimeline.setOnFinished(event -> {
            state = State.MOVING;
            skillState = SkillState.COOLDOWN;
            basakerSkillState = BasakerSkillState.NORMAL;
            setStateImage();
            GlobalVar.getGamePane().getChildren().remove(bloodImageView); // 移除血图层
            startSkillCooldown();
            if (weapon != null)
            {
                weapon.getDamage().set((weapon.getDamage().get() / 2));
                weapon.getCoolTime().set(weapon.getCoolTime().get() * 2);
            }
        });

        shadowFade.setNode(skillShadow);
        shadowFade.setDuration(Duration.seconds(1));
        shadowFade.setFromValue(0.5);
        shadowFade.setToValue(0);
        shadowFade.setOnFinished(event -> GlobalVar.getGamePane().getChildren().remove(skillShadow));

        trailColorAdjust.setHue(-0.1); // 火红色
        trailColorAdjust.setContrast(0.5);
        trailColorAdjust.setSaturation(0.5);

    }

    @Override
    public void init() { // 初始化为移动状态，技能预备，可受伤
        state = State.MOVING;
        skillState = SkillState.READY;
        injuryState = InjuryState.NORMAL;
        basakerSkillState = BasakerSkillState.NORMAL;
        showingImage.set(bornImage);
        showingImageView.imageProperty().bind(showingImage);
    }

    @Override
    public void move(Set<GameInput> activeInputs) {
        double deltaX = 0, deltaY = 0;
        double speed = super.speed.get();
        if (getState() == State.MOVING || getState() == State.SKILL) {
            if (activeInputs.contains(GameInput.MOVE_UP))
                deltaY -= speed;
            if (activeInputs.contains(GameInput.MOVE_DOWN))
                deltaY += speed;
            if (activeInputs.contains(GameInput.MOVE_LEFT))
                deltaX -= speed;
            if (activeInputs.contains(GameInput.MOVE_RIGHT))
                deltaX += speed;
            if (activeInputs.contains(GameInput.SKILL) && getSkillState() == SkillState.READY) {
                activateSkill();
            }
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

    private void activateSkill() { // 技能：进入特殊状态
        state = State.SKILL;
        skillState = SkillState.ACTIVE;
        basakerSkillState = BasakerSkillState.ENRAGED;
        AudioManager.getInstance().playAudio("SoundEffects","SkillBasaker","displayAudio");
        if (weapon != null)
        {
            weapon.getDamage().set((weapon.getDamage().get() * 2));
            weapon.getCoolTime().set(weapon.getCoolTime().get() / 2); // 武器冷却时间减半
        }
            

        setStateImage();

        // 产生一个巨大的原地50%透明度虚影
        createInitialSkillShadow();
        GlobalVar.getGamePane().getChildren().add(bloodImageView); // 添加血图层到顶层

        skillTimeline.play();
    }

    private void createInitialSkillShadow() {
        skillShadow.setX(showingImageView.getX() - skillShadow.getFitWidth() / 2 + showingImageView.getFitHeight() / 2);
        skillShadow.setY(showingImageView.getY() - skillShadow.getFitHeight() / 2 + showingImageView.getFitHeight() / 2);
        shadowFade.play();
        GlobalVar.getGamePane().getChildren().add(skillShadow);
    }

    protected void startSkillCooldown() { // 进入技能冷却
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
            damage = basakerSkillState == BasakerSkillState.ENRAGED ? damage << 1 : damage; // 受到的伤害减半
            HP.set(HP.get() - damage); // 受到伤害，扣除生命值
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

    private void createDashTrail() { // 冲刺生成虚影
        // 根据玩家位置生成虚影
        ImageView trail = new ImageView(showingImageView.getImage());
        trail.setFitWidth(showingImageView.getFitWidth());
        trail.setFitHeight(showingImageView.getFitHeight());
        trail.setX(showingImageView.getX());
        trail.setY(showingImageView.getY());
        trail.setEffect(trailColorAdjust);
        trail.setOpacity(0.5);

        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), trail);
        fade.setFromValue(0.5);
        fade.setToValue(0);
        fade.setOnFinished(event -> GlobalVar.getGamePane().getChildren().remove(trail));
        fade.play();

        GlobalVar.getGamePane().getChildren().add(trail);
    }
}
