package com.mambastu.material.pojo.entity.player;

import com.mambastu.controller.input.comp.GameInput;
import com.mambastu.material.resource.ResourceManager;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class LaughPlayer extends BasePlayer {
    private Image bornImage;
    private Image readyImage;
    private Image dieImage;
    private double skillCD = 2;//second
    private double skillDeltaX;
    private double skillDeltaY;


    public LaughPlayer() {
        this.bornImage = ResourceManager.getInstance().getImg("bornImage", "Player", "Player1");
        this.readyImage = ResourceManager.getInstance().getImg("readyImage", "Player", "Player1");
        this.dieImage = ResourceManager.getInstance().getImg("dieImage", "Player", "Player1");
        this.invincibleTimer.setCycleCount(8); // 无敌帧循环8次
        this.invincibleTimer.setOnFinished(e -> {
            setInjuryState(InjuryState.NORMAL);
        });
    }

    @Override
    public void init() {//初始化为移动状态，技能预备，可受伤
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

        if (getState()==State.SKILL) {
            createDashTrail();
        }

        x.set(x.get() + deltaX);
        y.set(y.get() + deltaY);

        showingImageView.setX(x.get());
        showingImageView.setY(y.get());

        crossedBoundary(root);
    }

    @Override
    public void getHurt(Integer damage) {
        if (injuryState != InjuryState.INVINCIBLE) {
            HP.set(HP.get() - damage); // 受到伤害，扣除生命值
            setInjuryState(InjuryState.INVINCIBLE); // 进入无敌状态
            invincibleTimer.playFromStart();
        }
    }



    private void activateSkill(Set<GameInput> activeInputs) {//技能：根据玩家的输入向前免伤冲刺
        setState(State.SKILL);
        setSkillState(SkillState.ACTIVE);
        setInjuryState(InjuryState.INVINCIBLE); // 无敌状态，不受伤害
        skillDeltaX = 0;
        skillDeltaY = 0;

        boolean moveUp = activeInputs.contains(GameInput.MOVE_UP);
        boolean moveDown = activeInputs.contains(GameInput.MOVE_DOWN);
        boolean moveLeft = activeInputs.contains(GameInput.MOVE_LEFT);
        boolean moveRight = activeInputs.contains(GameInput.MOVE_RIGHT);


        if (moveUp) skillDeltaY -= speed * 7;
        if (moveDown) skillDeltaY += speed * 7;
        if (moveLeft) skillDeltaX -= speed * 7;
        if (moveRight) skillDeltaX += speed * 7;
        //平衡斜向移动时的距离
        if ((moveUp||moveDown) && (moveLeft || moveRight)) {
            skillDeltaX *= 1 / Math.sqrt(2);
            skillDeltaY *= 1 / Math.sqrt(2);
        }

        PauseTransition skillTimeline = new PauseTransition(Duration.seconds(0.1));
        skillTimeline.setOnFinished(event -> {
            setState(State.MOVING);
            setSkillState(SkillState.COOLDOWN);
            setInjuryState(InjuryState.NORMAL);
            startSkillCooldown();
        });
        skillTimeline.play();
    }

    private void startSkillCooldown() {//进入技能冷却
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

    public void setStateImage() {//根据技能的状态来设置图像
        ColorAdjust colorAdjust = new ColorAdjust();
        if (getSkillState() == SkillState.ACTIVE) {
            colorAdjust.setHue(1);
            showingImageView.setEffect(colorAdjust);
        } else if(getSkillState() == SkillState.READY) {
            showingImage.set(readyImage);
            showingImageView.setEffect(colorAdjust);
        } else {
            showingImage.set(bornImage);
            showingImageView.setEffect(null);
        }
    }

    private void createDashTrail() {//冲刺生成虚影
        //根据玩家位置生成虚影
        ImageView trail = new ImageView(showingImageView.getImage());
        trail.setFitWidth(showingImageView.getFitWidth());
        trail.setFitHeight(showingImageView.getFitHeight());
        trail.setX(showingImageView.getX());
        trail.setY(showingImageView.getY());

        //虚影特性： 变蓝  0.5透明度 淡出（时间0.5秒）
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
}
