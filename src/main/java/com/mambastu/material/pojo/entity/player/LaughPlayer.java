package com.mambastu.material.pojo.entity.player;

import com.mambastu.material.resource.ResourceManager;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LaughPlayer extends BasePlayer{
    private ImageView bornImageView;
    private ImageView dieImageView;

    public LaughPlayer() {
    }

    @Override
    public void init() {
        bornImageView = new ImageView(ResourceManager.getInstance().getImg("bornImage", "Player", "Player1"));
        dieImageView = new ImageView(ResourceManager.getInstance().getImg("dieImage", "Player", "Player1"));
        showingImageView = bornImageView;
    }

    public void die() {
        showingImageView = dieImageView;
    }

}
