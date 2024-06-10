package com.mambastu.material.pojo.entity.monster;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class BossMonster extends BaseMonster {
    private enum State {
        IDLE, MOVING, SHAKING
    }

    private State state;

    public BossMonster(String imageUrl) {

        imageView = new ImageView(imageUrl);
        this.state = State.IDLE;
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(3), event -> setState(State.SHAKING)),
                new KeyFrame(Duration.seconds(3.5), event -> setState(State.MOVING)),
                new KeyFrame(Duration.seconds(4), event -> setState(State.IDLE)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
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
            imageView.setX(x.get());
            imageView.setY(y.get());
        } else if (state == State.SHAKING) {
            double shakingDistance = Math.random() - 0.5;
            x.set(x.get() + shakingDistance * imageView.getFitWidth() * 0.4);
            y.set(y.get() + shakingDistance * imageView.getFitHeight() * 0.4);
            imageView.setX(x.get());
            imageView.setY(y.get());
        }

    }

    public State getState() {
        return state;
    }
}
