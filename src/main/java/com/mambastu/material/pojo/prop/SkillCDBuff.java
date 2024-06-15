package com.mambastu.material.pojo.prop;

import com.mambastu.material.pojo.entity.player.BasePlayer;
import com.mambastu.material.pojo.weapon.BaseWeapon;
import com.mambastu.material.resource.ResourceManager;

public class SkillCDBuff extends BaseProp{

    public SkillCDBuff() {
        this.displayImage = ResourceManager.getInstance().getImg("displayImage", "Prop", "SkillCDBuff");
        this.price = 10;
        this.buffValue = 0.8;
    }

    @Override
    public void updateValue(BasePlayer player) {
        player.setSkillCD(player.getSkillCD() * buffValue);
    }
}
