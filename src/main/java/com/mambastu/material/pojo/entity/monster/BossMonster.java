package com.mambastu.material.pojo.entity.monster;

import com.mambastu.material.resource.ImgManager;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BossMonster extends BaseMonster{
    public BossMonster(String imageUrl) {
        imageView = new ImageView(imageUrl);
        System.out.println("创建成功 " + imageUrl);
    }

    @Override
    public void move(double targetX, double targetY) {
        speed = 1.0;
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
