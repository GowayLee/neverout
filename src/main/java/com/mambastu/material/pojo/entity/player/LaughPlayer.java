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

import java.util.Set;

public class LaughPlayer extends BasePlayer {
    private Image bornImage;
    private Image dieImage;
    private double skillDeltaX;
    private double skillDeltaY;

    private final PauseTransition skillTimeline;

    public LaughPlayer() {
        super();
        this.bornImage = ResourceManager.getInstance().getImg("bornImage", "Player", "LaughPlayer");
        this.dieImage = ResourceManager.getInstance().getImg("dieImage", "Player", "LaughPlayer");
        setImageSize(50, 50);
        this.skillTimeline = new PauseTransition(Duration.seconds(0.1));
        this.skillTimeline.setOnFinished(event -> {
            state = State.MOVING;
            injuryState = InjuryState.NORMAL;
            startSkillCooldown();
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
            createDashTrail(root);
        }

        x.set(x.get() + deltaX);
        y.set(y.get() + deltaY);

        showingImageView.setX(x.get());
        showingImageView.setY(y.get());

        trappedInStage();
    }

    private void activateSkill(Set<GameInput> activeInputs) {// 技能：根据玩家的输入向前免伤冲刺
        state = State.SKILL;
        skillState = SkillState.ACTIVE;
        injuryState = InjuryState.INVINCIBLE; // 无敌状态，不受伤害

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
        skillTimeline.play();
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
