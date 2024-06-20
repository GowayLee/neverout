package com.mambastu.gameobjects.prop;

import com.mambastu.gameobjects.entity.player.BasePlayer;
import com.mambastu.gameobjects.weapon.BaseWeapon;
import com.mambastu.resource.media.impl.ImageManager;

public class FireCDBuff extends BaseProp{

    public FireCDBuff() {
        this.displayImage = ImageManager.getInstance().getImg("displayImage", "Prop", "FireCDBuff");
        this.price = 10;
        this.buffValue = -0.15;
    }

    @Override
    public void updateValue(BasePlayer player) {
        BaseWeapon weapon = player.getWeapon();
        if (weapon != null) {
            weapon.getCoolTime().set(weapon.getCoolTime().get() * (buffValue + 1.0));
            weapon.setCoolTimeBuff(weapon.getCoolTimeBuff() + buffValue);
        }
    }
}
