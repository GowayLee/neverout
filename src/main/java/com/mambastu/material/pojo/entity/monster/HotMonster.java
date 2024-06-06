package com.mambastu.material.pojo.entity.monster;

import com.mambastu.material.resource.ImgManager;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HotMonster extends BaseMonster {

    public HotMonster() {
        Image image = ImgManager.getImage("/static/image/fever.png");
        imageView = new ImageView(image);
    }

    @Override
    public void move(double targetX, double targetY) {
        speed = 1.2;
        double dx = targetX - x;
        double dy = targetY - y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance > 0) {
            x += speed * dx / distance;
            y += speed * dy / distance;
        }
        imageView.setX(x);
        imageView.setY(y);
    }
}
