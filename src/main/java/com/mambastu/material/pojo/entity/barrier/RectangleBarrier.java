package com.mambastu.material.pojo.entity.barrier;

import com.mambastu.material.resource.ResourceManager;

import javafx.scene.image.Image;

public class RectangleBarrier extends BaseBarrier {
    private Image bornImage;


    public RectangleBarrier() {
        this.bornImage = ResourceManager.getInstance().getImg("bornImage", "Player", "Player1");
    }

    @Override
    public void init() {
        showingImage.set(bornImage);
        showingImageView.imageProperty().bind(showingImage);
    }
}
