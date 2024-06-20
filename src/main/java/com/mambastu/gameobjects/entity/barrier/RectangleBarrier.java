package com.mambastu.gameobjects.entity.barrier;

import com.mambastu.resource.media.impl.ImageManager;

import javafx.scene.image.Image;

public class RectangleBarrier extends BaseBarrier {
    private Image bornImage;


    public RectangleBarrier() {
        this.bornImage = ImageManager.getInstance().getImg("bornImage", "Player", "Player1");
    }

    @Override
    public void init() {
        showingImage.set(bornImage);
        showingImageView.imageProperty().bind(showingImage);
    }
}
