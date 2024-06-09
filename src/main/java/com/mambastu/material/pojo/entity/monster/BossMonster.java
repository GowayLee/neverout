package com.mambastu.material.pojo.entity.monster;

import com.mambastu.material.resource.ImgManager;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.Random;

public class BossMonster extends BaseMonster{
    private enum State {IDLE, MOVING,SHAKING}
    private State state;

    public BossMonster() {
        Image image = ImgManager.getImage("/static/image/boss.png");
        imageView = new ImageView(image);
        this.state = State.IDLE;
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(5), event -> setState(State.SHAKING)),
                new KeyFrame(Duration.seconds(5.5), event -> setState(State.MOVING)),
                new KeyFrame(Duration.seconds(6), event -> setState(State.IDLE))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void setState(State state) {
        this.state = state;
    }

    @Override
    public void move(double targetX, double targetY) {
        double SPEED = 5;
        if (state == State.MOVING) {
            double dx = targetX - x;
            double dy = targetY - y;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance > 0) {
                x += SPEED * dx / distance;
                y += SPEED * dy / distance;
            }

            imageView.setX(x);
            imageView.setY(y);
        }

        if (state == State.SHAKING) {
            double shakingDistance = Math.random()-0.5;
            x += shakingDistance*imageView.getFitWidth()*0.3;
            y += shakingDistance*imageView.getFitHeight()*0.3;
            imageView.setX(x);
            imageView.setY(y);
        }
    }







}
