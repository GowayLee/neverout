package com.mambastu.material.pojo.entity.player;

import com.mambastu.material.resource.ResourceManager;

import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LaughPlayer extends BasePlayer{
    private Image bornImage;
    private Image dieImage;

    public LaughPlayer() {
        this.bornImage = ResourceManager.getInstance().getImg("bornImage", "Player", "Player1");
        this.dieImage = ResourceManager.getInstance().getImg("dieImage", "Player", "Player1");
    }

    @Override
    public void init() {
        showingImage.set(bornImage);
        showingImageView.imageProperty().bind(showingImage);
    }

    public void die() {
        showingImage.set(dieImage);
    }

}
