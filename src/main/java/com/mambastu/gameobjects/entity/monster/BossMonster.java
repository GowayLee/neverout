package com.mambastu.gameobjects.entity.monster;

import java.util.List;

import com.mambastu.factories.MonsterFactory;
import com.mambastu.resource.media.impl.ImageManager;
import com.mambastu.utils.BetterMath;
import com.mambastu.utils.GlobalVar;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class BossMonster extends BaseMonster {
    private final Image omenImage;
    private final Image bornImage;
    private final Image attackImage;
    private final Image dieImage;

    private final Timeline moveTimer;
    private MoveState moveState;

    private enum MoveState {
        IDLE, SHAKING, MOVING
    }

    public BossMonster() {
        super();
        this.omenImage = ImageManager.getInstance().getImg("omenImage", "Monster", "BossMonster");
        this.bornImage = ImageManager.getInstance().getImg("bornImage", "Monster", "BossMonster");
        this.attackImage = ImageManager.getInstance().getImg("attackImage", "Monster", "BossMonster");
        this.dieImage = ImageManager.getInstance().getImg("dieImage", "Monster", "BossMonster");
        setImageSize(50, 50);
        super.damage = 20;
        super.initTimer.setDuration(Duration.seconds(1));
        this.moveTimer = new Timeline(
                new KeyFrame(Duration.seconds(4.0), event -> {
                    moveState = MoveState.SHAKING;
                    showingImage.set(attackImage);
                }),
                new KeyFrame(Duration.seconds(4.5), event -> moveState = MoveState.MOVING),
                new KeyFrame(Duration.seconds(5.0), event -> {
                    moveState = MoveState.IDLE;
                    showingImage.set(bornImage);
                }));
        moveTimer.setCycleCount(Timeline.INDEFINITE);
    }

    @Override
    public void init() {
        HP.set(200);
        state = State.OMEN;
        inBulletQueue.clear();
        showingImage.set(omenImage);
        showingImageView.imageProperty().bind(showingImage);
    }

    @Override
    public void omen(List<BaseMonster> monsterList) {
        initTimer.setOnFinished(event -> {
            state = State.NORMAL;
            moveState = MoveState.IDLE;
            moveTimer.playFromStart();
            showingImage.set(bornImage);
            monsterList.add(this);
        });
        initTimer.playFromStart();
    }

    @Override
    public void move(double targetX, double targetY) {
        if (state == State.NORMAL) {
            speed = 10.0;
            switch (moveState) {
                case MOVING:
                    double dx = targetX - x.get();
                    double dy = targetY - y.get();
                    double distance = BetterMath.sqrt(dx * dx + dy * dy);
                    if (distance > 0) {
                        x.set(x.get() + speed * dx / distance);
                        y.set(y.get() + speed * dy / distance);
                    }
                    showingImageView.setX(x.get());
                    showingImageView.setY(y.get());
                    trappedInStage();
                    break;
                case SHAKING:
                    double shakingDistance = Math.random() - 0.5;
                    x.set(x.get() + shakingDistance * showingImageView.getFitWidth() * 0.4);
                    y.set(y.get() + shakingDistance * showingImageView.getFitHeight() * 0.4);
                    showingImageView.setX(x.get());
                    showingImageView.setY(y.get());
                    trappedInStage();
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public Integer releaseDamage() {
        return state == State.NORMAL ? damage : 0;
    }

    @Override
    public void die() {
        state = State.DIE;
        moveTimer.stop();
        showingImage.set(dieImage);
        PauseTransition rmTimer = new PauseTransition(Duration.seconds(1));
        rmTimer.setOnFinished(e -> {
            removeFromPane(GlobalVar.getGamePane()); // Remove from pane after 1 second.
            MonsterFactory.getInstance().delete(this);
        });
        rmTimer.play();
    }
}
