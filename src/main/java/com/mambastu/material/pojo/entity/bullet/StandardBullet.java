package com.mambastu.material.pojo.entity.bullet;

import com.mambastu.material.resource.ResourceManager;

import javafx.scene.image.Image;

public class StandardBullet extends BaseBullet{
    private Image bornImage;

    public StandardBullet() {
        this.bornImage = ResourceManager.getInstance().getImg("bornImage", "Player", "Player1");
    }

    @Override
    public void init() {
        showingImage.set(bornImage);
        showingImageView.imageProperty().bind(showingImage);
        setImageSize(20, 20);
    }

    @Override
    public void move() {
        double targetX = target.getX().get();
        double targetY = target.getY().get();
        double currentX = x.get();
        double currentY = y.get();

        double dx = targetX - currentX;
        double dy = targetY - currentY;
        double squaredDistance = dx * dx + dy * dy;
        double squaredSpeed = speed * speed;
    
        if (squaredDistance > 0 && squaredDistance > squaredSpeed) {
            double scale = speed / Math.sqrt(squaredDistance);
            currentX += dx * scale;
            currentY += dy * scale;
        } else if (squaredDistance > 0) { // 到达目标位置
            currentX = targetX;
            currentY = targetY;
        }

        x.set(currentX);
        y.set(currentY);
        showingImageView.setX(currentX);
        showingImageView.setY(currentY);
    }

    @Override
    public boolean isHitTarget() {
        return (x.get() == target.getX().get() && y.get() == target.getY().get());
    }

}
