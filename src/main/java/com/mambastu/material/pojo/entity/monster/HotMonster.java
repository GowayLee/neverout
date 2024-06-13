package com.mambastu.material.pojo.entity.monster;

import com.mambastu.material.resource.ResourceManager;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class HotMonster extends BaseMonster {
    private final Image bornImage;
    private final Image omenImage;
    private Timeline timeline;



    public HotMonster() {
        this.bornImage = ResourceManager.getInstance().getImg("bornImage", "Monster", "HotMonster");
        this.omenImage = ResourceManager.getInstance().getImg("omenImage", "Monster", "HotMonster");
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event ->{
                    setState(State.MOVING);
                    showingImage.set(bornImage);
                })
        );
        timeline.setCycleCount(1);
    }

    @Override
    public void init() {
        timeline.playFromStart();
        showingImage.set(omenImage);
        showingImageView.imageProperty().bind(showingImage);
        setState(State.OMEN);
    }



    @Override
    public void move(double targetX, double targetY) {
        savePreviousFrame();
        if (getState() == HotMonster.State.MOVING){
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
