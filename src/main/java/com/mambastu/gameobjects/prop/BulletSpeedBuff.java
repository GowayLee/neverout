package com.mambastu.gameobjects.prop;

import com.mambastu.gameobjects.entity.player.BasePlayer;
import com.mambastu.gameobjects.weapon.BaseWeapon;
import com.mambastu.resource.media.impl.ImageManager;

public class BulletSpeedBuff extends BaseProp {

    public BulletSpeedBuff() {
        this.displayImage = ImageManager.getInstance().getImg("displayImage", "Prop", "FireCDBuff");
        this.price = 10;
        this.buffValue = 0.05;
    }

    @Override
    public void updateValue(BasePlayer player) {
        BaseWeapon weapon = player.getWeapon();
        if (weapon != null) {
            weapon.getBulletSpeed().set(weapon.getBulletSpeed().get() * (buffValue + 1.0));
            weapon.setBulletSpeedBuff(weapon.getBulletSpeedBuff() + buffValue);
        }
    }
}
