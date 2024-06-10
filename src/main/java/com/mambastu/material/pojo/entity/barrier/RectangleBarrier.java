package com.mambastu.material.pojo.entity.barrier;

import com.mambastu.material.pojo.entity.BaseEntity;
import com.mambastu.material.resource.ImgManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RectangleBarrier extends BaseBarrier {

    public RectangleBarrier() {
        Image image = ImgManager.getImage("/static/image/stone.png");
        imageView = new ImageView(image);
    }


    @Override
    public Bounds getBounds() {
        return null;
    }
}
