package com.mambastu.material.pojo.prop;

import com.mambastu.material.pojo.entity.player.BasePlayer;
import com.mambastu.material.resource.ResourceManager;

public class SpeedBuff extends BaseProp {

    public SpeedBuff() {
        this.displayImage = ResourceManager.getInstance().getImg("displayImage", "Prop", "SpeedBuff");
        this.price = 10;
        this.buffValue = 1;
    }

    @Override
    public void updateValue(BasePlayer player) {
        player.getSpeed().set(player.getSpeed().get() + buffValue);
    }
}
