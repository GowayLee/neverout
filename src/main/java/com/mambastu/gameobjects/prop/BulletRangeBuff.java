package com.mambastu.gameobjects.prop;

import com.mambastu.gameobjects.entity.player.BasePlayer;
import com.mambastu.gameobjects.weapon.BaseWeapon;
import com.mambastu.resource.media.impl.ImageManager;

public class BulletRangeBuff extends BaseProp {

    public BulletRangeBuff() {
        this.displayImage = ImageManager.getInstance().getImg("displayImage", "Prop", "FireCDBuff");
        this.price = 10;
        this.buffValue = 0.15;
    }

    @Override
    public void updateValue(BasePlayer player) {
        BaseWeapon weapon = player.getWeapon();
        if (weapon != null) {
            weapon.getRange().set(weapon.getRange().get() * (buffValue + 1.0));
            weapon.setRangeBuff(weapon.getRangeBuff() + buffValue);
        }
    }
}
