package com.mambastu.material.pojo.entity.barrier;

import com.mambastu.material.resource.ResourceManager;

import javafx.scene.image.ImageView;

public class RectangleBarrier extends BaseBarrier {
    private ImageView bornImageView;


    public RectangleBarrier() {
    }

    @Override
    public void init() {
        bornImageView = new ImageView(ResourceManager.getInstance().getImg("bornImage", "Player", "Player1"));
        showingImageView = bornImageView;
    }


    @Override
    public Bounds getBounds() {
        return null;
    }
}
