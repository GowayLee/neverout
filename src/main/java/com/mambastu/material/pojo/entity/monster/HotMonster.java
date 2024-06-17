package com.mambastu.material.pojo.entity.monster;

import java.util.List;

import com.mambastu.factories.MonsterFactory;
import com.mambastu.material.resource.ResourceManager;
import com.mambastu.util.BetterMath;
import com.mambastu.util.GlobalVar;

import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class HotMonster extends BaseMonster {
    private final Image bornImage;
    private final Image omenImage;
    private final Image dieImage;

    public HotMonster() {
        super();
        this.bornImage = ResourceManager.getInstance().getImg("bornImage", "Monster", "HotMonster");
        this.omenImage = ResourceManager.getInstance().getImg("omenImage", "Monster", "HotMonster");
        this.dieImage = ResourceManager.getInstance().getImg("dieImage", "Monster", "HotMonster");
        setImageSize(50, 50);
        this.damage = 10;
        this.initTimer.setDuration(Duration.seconds(1));
    }

    @Override
    public void init() {
        HP.set(100);
        inBulletQueue.clear();
        showingImage.set(omenImage);
        showingImageView.imageProperty().bind(showingImage);
        state = State.OMEN;
    }

    @Override
    public void omen(List<BaseMonster> monsterList) {
        initTimer.setOnFinished(event -> {
            state = State.NORMAL;
            showingImage.set(bornImage);
            monsterList.add(this);
        });
        initTimer.playFromStart();
    }

    @Override
    public void move(double targetX, double targetY) {
        if (state == HotMonster.State.NORMAL) {
            speed = 1.2;
            double dx = targetX - x.get();
            double dy = targetY - y.get();
            double dl = BetterMath.sqrt(dx * dx + dy * dy);
            if (dl > 0) {
                x.set(x.get() + speed * dx / dl);
                y.set(y.get() + speed * dy / dl);
            }
            showingImageView.setX(x.get());
            showingImageView.setY(y.get());
        }
        trappedInStage();
    }

    @Override
    public Integer releaseDamage() {
        return state == State.NORMAL ? damage : 0;
    }

    @Override
    public void die() {
        state = State.DIE;
        showingImage.set(dieImage);
        PauseTransition rmTimer = new PauseTransition(Duration.seconds(1));
        rmTimer.setOnFinished( e ->{
            removeFromPane(GlobalVar.getGamePane()); // Remove from pane after 1 second.
            MonsterFactory.getInstance().delete(this);
        });
        rmTimer.play();
    }

}
