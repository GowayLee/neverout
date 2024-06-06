package com.mambastu.material.pojo.entity.player;

import com.mambastu.material.resource.ImgManager;

import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player extends BasePlayer{
    public Player() {
        imageView = new ImageView(ImgManager.getImage("/static/image/player1.png"));
    }

    public void die() {
        imageView.setImage(ImgManager.getImage("/static/image/player_die.png"));
    }

}
