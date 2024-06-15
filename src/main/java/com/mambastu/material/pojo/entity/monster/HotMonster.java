package com.mambastu.material.pojo.entity.monster;

import java.util.List;

import com.mambastu.factories.MonsterFactory;
import com.mambastu.material.resource.ResourceManager;

import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class HotMonster extends BaseMonster {
    private final Image bornImage;
    private final Image omenImage;
    private final Image dieImage;

    public HotMonster() {
        super();
        this.bornImage = ResourceManager.getInstance().getImg("bornImage", "Monster", "HotMonster");
        this.omenImage = ResourceManager.getInstance().getImg("omenImage", "Monster", "HotMonster");
        this.dieImage = ResourceManager.getInstance().getImg("bornImage", "Player", "Player1");
        setImageSize(50, 50);
        this.damage = 10;
        this.initTimer.setDuration(Duration.seconds(1));
    }

    @Override
    public void init() {
        HP.set(100);
        showingImage.set(omenImage);
        showingImageView.imageProperty().bind(showingImage);
        setState(State.OMEN);
    }

    @Override
    public void omen(List<BaseMonster> monsterList) {
        initTimer.setOnFinished(event -> {
            setState(State.MOVING);
            showingImage.set(bornImage);
            monsterList.add(this);
        });
        initTimer.playFromStart();
    }

    @Override
    public void move(double targetX, double targetY) {
        savePreviousFrame();
        if (getState() == HotMonster.State.MOVING) {
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

    @Override
    public Integer releaseDamage() {
        if (state != State.OMEN) {
            return damage;
        }
        return 0;
    }

    @Override
    public void die(Pane root) {
        showingImage.set(dieImage);
        PauseTransition rmTimer = new PauseTransition(Duration.seconds(1));
        rmTimer.setOnFinished( e ->{
            removeFromPane(root); // Remove from pane after 1 second.
            MonsterFactory.getInstance().delete(this);
        });
        rmTimer.play();
    }

}
