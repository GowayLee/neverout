package com.mambastu.gameobjects.prop;

import com.mambastu.gameobjects.entity.player.BasePlayer;
import com.mambastu.resource.media.impl.ImageManager;

public class HpBuff extends BaseProp {

    public HpBuff() {
        this.displayImage = ImageManager.getInstance().getImg("displayImage", "Prop", "HpBuff");
        this.price = 10;
        this.buffValue = 15;
    }

    @Override
    public void updateValue(BasePlayer player) {
        player.getMaxHP().set(player.getMaxHP().get() + (int) buffValue);
    }
}
