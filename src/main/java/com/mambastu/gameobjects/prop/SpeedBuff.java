package com.mambastu.gameobjects.prop;

import com.mambastu.gameobjects.entity.player.BasePlayer;
import com.mambastu.resource.media.impl.ImageManager;

public class SpeedBuff extends BaseProp {

    public SpeedBuff() {
        this.displayImage = ImageManager.getInstance().getImg("displayImage", "Prop", "SpeedBuff");
        this.price = 10;
        this.buffValue = 1;
    }

    @Override
    public void updateValue(BasePlayer player) {
        player.getSpeed().set(player.getSpeed().get() + buffValue);
    }
}
