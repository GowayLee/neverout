package com.mambastu.material.pojo.entity.monster;

import com.mambastu.material.pojo.Interface.Movable;
import com.mambastu.material.pojo.enums.CollisionState;
import com.mambastu.material.resource.ResourceManager;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class BossMonster extends BaseMonster {
    private final Timeline timeline;
    private final Timeline initTimeline;
    private final Image omenImage;
    private final Image bornImage;
    private final Image attackImage;


    public BossMonster() {
        this.bornImage = ResourceManager.getInstance().getImg("bornImage", "Monster", "BossMonster");
        this.attackImage = ResourceManager.getInstance().getImg("attackImage", "Monster", "BossMonster");
        this.omenImage = ResourceManager.getInstance().getImg("omenImage", "Monster", "BossMonster");
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

        this.initTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    setState(State.IDLE);
                    showingImage.set(bornImage);
                    timeline.playFromStart();
                })
        );
        initTimeline.setCycleCount(1);
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    @Override
    public void init() {
        initTimeline.playFromStart();
        setState(State.OMEN);
        showingImage.set(omenImage);
        showingImageView.imageProperty().bind(showingImage);
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
        } else if (getState() == State.SHAKING) {
            double shakingDistance = Math.random() - 0.5;
            x.set(x.get() + shakingDistance * showingImageView.getFitWidth() * 0.4);
            y.set(y.get() + shakingDistance * showingImageView.getFitHeight() * 0.4);
            showingImageView.setX(x.get());
            showingImageView.setY(y.get());
            crossedBoundary();
        }

    }
}
