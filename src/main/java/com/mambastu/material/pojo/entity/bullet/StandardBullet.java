package com.mambastu.material.pojo.entity.bullet;

import com.mambastu.material.resource.ResourceManager;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class StandardBullet extends BaseBullet{
    private Image bornImage;
    private double dx;
    private double dy;
    private double scale;

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
        if (target.isOnStage()) { // 如果目标还活着，继续追踪目标移动子弹
            double targetX = target.getX().get();
            double targetY = target.getY().get();
            double currentX = x.get();
            double currentY = y.get();

            dx = targetX - currentX;
            dy = targetY - currentY;
            double squaredDistance = dx * dx + dy * dy;
            double squaredSpeed = speed * speed;
        
            if (squaredDistance > 0 && squaredDistance > squaredSpeed) {
                scale = speed / Math.sqrt(squaredDistance);
                currentX += dx * scale;
                currentY += dy * scale;
                range -= speed; // 减少子弹的射程
            } else if (squaredDistance > 0) { // 到达目标位置
                currentX = targetX;
                currentY = targetY;
            }

            x.set(currentX);
            y.set(currentY);
            showingImageView.setX(currentX);
            showingImageView.setY(currentY);
        } else { // 如果目标已经死亡，子弹沿着最后一次更新的方向直线移动 // TODO: 改进移动方法
            double currentX = x.get() + speed / 1.414;
            double currentY = y.get() + speed / 1.414;
            range -= speed;
    
            x.set(currentX);
            y.set(currentY);
            showingImageView.setX(currentX);
            showingImageView.setY(currentY);
        }
        isValid = !trappedInStage() && target.isOnStage() && range > 0 ; // 判断子弹是否有效
    }

    @Override
    public void afterHitTarget() {
        isValid = false;
    }

}
