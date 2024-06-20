package com.mambastu.gameobjects.entity.bullet;

import com.mambastu.resource.media.impl.AudioManager;
import com.mambastu.resource.media.impl.ImageManager;

import javafx.scene.image.Image;

public class StandardBullet extends BaseBullet{
    private Image bornImage;

    public StandardBullet() {
        super();
        this.bornImage = ImageManager.getInstance().getImg("bornImage", "Bullet", "StandardBullet");
        setImageSize(25, 25);
    }

    @Override
    public void init() {
        isValid = true;
        showingImage.set(bornImage);
        showingImageView.imageProperty().bind(showingImage);
    }

    @Override
    public void move() {
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
    public Integer releaseDamage() {
        AudioManager.getInstance().playAudio("SoundEffects", "StandardBulletHit", "displayAudio");
        return damage;
    }

    @Override
    public void afterHitTarget() {
        isValid = false;
    }
}
