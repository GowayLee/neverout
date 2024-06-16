package com.mambastu.material.pojo.entity.bullet;

import com.mambastu.material.resource.ResourceManager;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class StandardBullet extends BaseBullet{
    private Image bornImage;

    public StandardBullet() {
        super();
        this.bornImage = ResourceManager.getInstance().getImg("bornImage", "Player", "Player1");
        setImageSize(20, 20);
    }

    @Override
    public void init() {
        isValid = true;
        showingImage.set(bornImage);
        showingImageView.imageProperty().bind(showingImage);
    }

    @Override
    public void move(Pane root) {
        double currentX = x.get() + speed * sin;
        double currentY = y.get() + speed * cos;
        range -= speed; // 减少子弹的射程

        x.set(currentX);
        y.set(currentY);
        showingImageView.setX(currentX);
        showingImageView.setY(currentY);
        isValid = !trappedInStage() && range > 0; // 判断子弹是否有效
    }

    @Override
    public void afterHitTarget() {
        isValid = false;
    }
}
