package com.mambastu.material.pojo.entity.monster;

import com.mambastu.material.resource.ResourceManager;

import javafx.scene.image.ImageView;

public class HotMonster extends BaseMonster {

    public HotMonster() {
    }

    @Override
    public void init() {
        showingImageView = new ImageView(ResourceManager.getInstance().getImg("bornImage", "Monster", "HotMonster"));
    }

    @Override
    public void move(double targetX, double targetY) {
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
}
