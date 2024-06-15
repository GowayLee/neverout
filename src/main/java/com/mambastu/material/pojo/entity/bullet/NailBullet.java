package com.mambastu.material.pojo.entity.bullet;

import com.mambastu.material.pojo.entity.BaseEntity;
import com.mambastu.material.resource.ResourceManager;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class NailBullet extends BaseBullet{
    private Image bornImage;
    private double dx;
    private double dy;
    private double scale;

    public NailBullet() {
        super();
        this.bornImage = ResourceManager.getInstance().getImg("bornImage", "Player", "Player1");
        setImageSize(15, 15);
    }

    @Override
    public void init() {
        showingImage.set(bornImage);
        showingImageView.imageProperty().bind(showingImage);
    }

    @Override
    public void setTarget(BaseEntity target) {
        dx = target.getX().get() - x.get();
        dy = target.getY().get() - y.get();
        scale = speed / Math.sqrt(dx * dx + dy * dy); // 计算缩放比例，使得子弹移动速度为speed
    }

    @Override
    public void move(Pane root) {
        double currentX = x.get() + dx * scale;
        double currentY = y.get() + dy * scale;
        range -= speed; // 减少子弹的射程

        x.set(currentX);
        y.set(currentY);
        showingImageView.setX(currentX);
        showingImageView.setY(currentY);
        trappedInStage();
    }

    @Override
    public boolean afterHitTarget() {
        return false;
    }
}
