package com.mambastu.material.pojo.entity.monster;

import com.mambastu.material.resource.ResourceManager;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class HotMonster extends BaseMonster {
    private final Image bornImage;

    private enum State {
        IDLE, MOVING
    }
    private State state;

    public HotMonster() {
        this.bornImage = ResourceManager.getInstance().getImg("bornImage", "Monster", "HotMonster");
    }

    @Override
    public void init() {
        showingImage.set(bornImage);
        showingImageView.imageProperty().bind(showingImage);
        this.state = HotMonster.State.IDLE;
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event ->this.state = State.MOVING)
        );
        timeline.setCycleCount(1);
        timeline.play();
    }



    @Override
    public void move(double targetX, double targetY) {
        savePreviousFrame();
        if (state == HotMonster.State.MOVING){
            speed = 1.2;
        double dx = targetX - x.get();
        double dy = targetY - y.get();
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance > 0) {
            x.set(x.get() + speed * dx / distance);
            y.set(y.get() + speed * dy / distance);
        }
        showingImageView.setX(x.get());
        showingImageView.setY(y.get());
        }
        crossedBoundary();
    }


}
