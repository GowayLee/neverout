package com.mambastu.material.pojo.entity.bullet;

import com.mambastu.material.resource.ResourceManager;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class NailBullet extends BaseBullet {
    private Image bornImage;

    public NailBullet() {
        super();
        this.bornImage = ResourceManager.getInstance().getImg("bornImage", "Bullet", "NailBullet");
        setImageSize(15, 15);
    }

    @Override
    public void init() {
        isValid = true;
        showingImage.set(bornImage);
        showingImageView.imageProperty().bind(showingImage);
    }

    @Override
    public void move(Pane root) {
        double currentX = x.get() + speed * cos;
        double currentY = y.get() + speed * sin;
        range -= speed; // 减少子弹的射程

        x.set(currentX);
        y.set(currentY);
        showingImageView.setX(currentX);
        showingImageView.setY(currentY);
        isValid = !trappedInStage() && range > 0; // 判断子弹是否有效
    }

    @Override
    public void afterHitTarget() {
        isValid = true;
    }
}
