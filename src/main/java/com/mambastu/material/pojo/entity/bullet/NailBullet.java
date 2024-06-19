package com.mambastu.material.pojo.entity.bullet;

import com.mambastu.material.resource.ResourceManager;
import com.mambastu.util.AudioManager;

import javafx.scene.image.Image;

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
        AudioManager.getInstance().playAudio("SoundEffects", "NailBulletHit", "displayAudio");
        return damage;
    }

    @Override
    public void afterHitTarget() {
        isValid = true;
    }
}
