package com.mambastu.material.pojo.entity.monster;

import com.mambastu.material.resource.ResourceManager;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class BossMonster extends BaseMonster {
    private enum State { IDLE, MOVING, SHAKING };
    private final Timeline timeline;

    private final Image bornImage;
    private final Image attackImage;
    private final Image dieImage;

    private State state;

    public BossMonster() {
        this.bornImage = ResourceManager.getInstance().getImg("bornImage", "Monster", "BossMonster");
        this.attackImage = ResourceManager.getInstance().getImg("attackImage", "Monster", "BossMonster");
        this.dieImage = ResourceManager.getInstance().getImg("bornImage", "Player", "Player1");
        this.state = State.IDLE;
        this.timeline = new Timeline(
                new KeyFrame(Duration.seconds(4.0), event -> {
                    setState(State.SHAKING);
                    showingImage.set(attackImage);
                }),
                new KeyFrame(Duration.seconds(4.5), event -> setState(State.MOVING)),
                new KeyFrame(Duration.seconds(5.0), event -> {
                    setState(State.IDLE);
                    showingImage.set(bornImage);
                }));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    @Override
    public void init() {
        HP.set(200);
        timeline.playFromStart();
        setState(State.IDLE);
        showingImage.set(bornImage);
        showingImageView.imageProperty().bind(showingImage);
    }

    private void setState(State state) {
        this.state = state;
    }

    @Override
    public void move(double targetX, double targetY) {
        speed = 10.0;
        if (state == State.MOVING) {
            double dx = targetX - x.get();
            double dy = targetY - y.get();
            double distance = Math.sqrt(dx * dx + dy * dy);
            if (distance > 0) {
                x.set(x.get() + speed * dx / distance);
                y.set(y.get() + speed * dy / distance);
            }
            showingImageView.setX(x.get());
            showingImageView.setY(y.get());
        } else if (state == State.SHAKING) {
            double shakingDistance = Math.random() - 0.5;
            x.set(x.get() + shakingDistance * showingImageView.getFitWidth() * 0.4);
            y.set(y.get() + shakingDistance * showingImageView.getFitHeight() * 0.4);
            showingImageView.setX(x.get());
            showingImageView.setY(y.get());
        }

    }

    @Override
    public void die() {
        showingImage.set(dieImage);
    }
}
