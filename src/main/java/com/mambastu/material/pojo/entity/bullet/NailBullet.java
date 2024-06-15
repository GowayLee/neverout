package com.mambastu.material.pojo.entity.bullet;

import com.mambastu.material.pojo.entity.BaseEntity;
import com.mambastu.material.resource.ResourceManager;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class NailBullet extends BaseBullet {
    private Image bornImage;
    private double dx;
    private double dy;
    private double sin;
    private double cos;

    public NailBullet() {
        super();
        this.bornImage = ResourceManager.getInstance().getImg("bornImage", "Player", "Player1");
        setImageSize(15, 15);
    }

    @Override
    public void init() {
        isValid = true;
        showingImage.set(bornImage);
        showingImageView.imageProperty().bind(showingImage);
    }

    @Override
    public void setTarget(BaseEntity target, double offsetSin) {
        dx = target.getX().get() - x.get();
        dy = target.getY().get() - y.get();
        super.offsetSin = offsetSin;
        super.offsetCos = Math.sqrt(1.0 - offsetSin * offsetSin); // 预先计算偏移夹角的Cos值，用于计算弹道
        double dl = Math.sqrt(dx * dx + dy * dy);
        double sina = dx / dl;
        double cosa = dy / dl;
        sin = offsetSin * cosa + sina * offsetCos;
        cos = offsetCos * cosa - offsetSin * sina;
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
        isValid = true;
    }
}
