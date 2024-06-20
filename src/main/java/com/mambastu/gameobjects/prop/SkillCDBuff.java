package com.mambastu.gameobjects.prop;

import com.mambastu.gameobjects.entity.player.BasePlayer;
import com.mambastu.resource.media.impl.ImageManager;

public class SkillCDBuff extends BaseProp {

    public SkillCDBuff() {
        this.displayImage = ImageManager.getInstance().getImg("displayImage", "Prop", "SkillCDBuff");
        this.price = 10;
        this.buffValue = -0.2;
    }

    @Override
    public void updateValue(BasePlayer player) {
        player.getSkillCD().set(player.getSkillCD().get() * (buffValue + 1.0) > 1.0 ? player.getSkillCD().get() * (buffValue + 1.0) : 1.0);
    }
}
