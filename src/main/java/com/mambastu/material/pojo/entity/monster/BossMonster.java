package com.mambastu.material.pojo.entity.monster;

import javafx.scene.image.ImageView;

public class BossMonster extends BaseMonster{
    public BossMonster(String imageUrl) {
        imageView = new ImageView(imageUrl);
    }

    @Override
    public void move(double targetX, double targetY) {
        speed = 1.0;
        double dx = targetX - x.get();
        double dy = targetY - y.get();
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance > 0) {
            x.set(x.get() + speed * dx / distance);
            y.set(y.get() + speed * dy / distance);
        }
        imageView.setX(x.get());
        imageView.setY(y.get());
    }

}
