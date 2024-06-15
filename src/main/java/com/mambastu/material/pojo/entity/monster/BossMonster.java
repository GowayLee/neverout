package com.mambastu.material.pojo.entity.monster;

import java.util.List;

import com.mambastu.factories.MonsterFactory;
import com.mambastu.material.resource.ResourceManager;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class BossMonster extends BaseMonster {
    private final Timeline moveTimer;
    private final Image omenImage;
    private final Image bornImage;
    private final Image attackImage;
    private final Image dieImage;


    public BossMonster() {
        super();
        this.bornImage = ResourceManager.getInstance().getImg("bornImage", "Monster", "BossMonster");
        this.attackImage = ResourceManager.getInstance().getImg("attackImage", "Monster", "BossMonster");
        this.dieImage = ResourceManager.getInstance().getImg("bornImage", "Player", "Player1");
        this.omenImage = ResourceManager.getInstance().getImg("omenImage", "Monster", "BossMonster");
        setImageSize(50, 50);
        this.damage = 20;
        this.moveTimer = new Timeline(
                new KeyFrame(Duration.seconds(4.0), event -> {
                    setState(State.SHAKING);
                    showingImage.set(attackImage);
                }),
                new KeyFrame(Duration.seconds(4.5), event -> setState(State.MOVING)),
                new KeyFrame(Duration.seconds(5.0), event -> {
                    setState(State.IDLE);
                    showingImage.set(bornImage);
                }));
        moveTimer.setCycleCount(Timeline.INDEFINITE);
        this.initTimer.setDuration(Duration.seconds(1));
    }

    @Override
    public void init() {
        HP.set(200);
        setState(State.OMEN);
        showingImage.set(omenImage);
        showingImageView.imageProperty().bind(showingImage);
    }

    @Override
    public void omen(List<BaseMonster> monsterList) {
        initTimer.setOnFinished(event -> {
            setState(State.IDLE);
            moveTimer.playFromStart();
            showingImage.set(bornImage);
            monsterList.add(this);
        });
        initTimer.playFromStart();
    }

    @Override
    public void move(double targetX, double targetY) {
        speed = 10.0;
        savePreviousFrame();
        if (getState() == State.MOVING) {
            double dx = targetX - x.get();
            double dy = targetY - y.get();
            double distance = Math.sqrt(dx * dx + dy * dy);
            if (distance > 0) {
                x.set(x.get() + speed * dx / distance);
                y.set(y.get() + speed * dy / distance);
            }
            showingImageView.setX(x.get());
            showingImageView.setY(y.get());
            crossedBoundary();
        } else if (getState() == State.SHAKING) {
            double shakingDistance = Math.random() - 0.5;
            x.set(x.get() + shakingDistance * showingImageView.getFitWidth() * 0.4);
            y.set(y.get() + shakingDistance * showingImageView.getFitHeight() * 0.4);
            showingImageView.setX(x.get());
            showingImageView.setY(y.get());
            crossedBoundary();
        }

    }

    @Override
    public Integer releaseDamage() {
        if (state != State.OMEN) {
            return damage;
        }
        return 0;
    }

    @Override
    public void die(Pane root) {
        moveTimer.stop();
        showingImage.set(dieImage);
        PauseTransition rmTimer = new PauseTransition(Duration.seconds(1));
        rmTimer.setOnFinished( e ->{
            removeFromPane(root); // Remove from pane after 1 second.
            MonsterFactory.getInstance().delete(this);
        });
        rmTimer.play();
    }
}
