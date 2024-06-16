package com.mambastu.material.pojo.prop;

import com.mambastu.material.pojo.entity.player.BasePlayer;
import com.mambastu.material.resource.ResourceManager;

public class HpBuff extends BaseProp {

    public HpBuff() {
        this.displayImage = ResourceManager.getInstance().getImg("displayImage", "Prop", "HpBuff");
        this.price = 10;
        this.buffValue = 10;
    }

    @Override
    public void updateValue(BasePlayer player) {
        player.getMaxHP().set(player.getMaxHP().get() + (int) buffValue);
    }
}
