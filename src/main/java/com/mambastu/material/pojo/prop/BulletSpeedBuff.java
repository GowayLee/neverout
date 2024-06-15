package com.mambastu.material.pojo.prop;

import com.mambastu.material.pojo.entity.player.BasePlayer;
import com.mambastu.material.pojo.weapon.BaseWeapon;
import com.mambastu.material.resource.ResourceManager;

public class BulletSpeedBuff extends BaseProp{

    public BulletSpeedBuff() {
        this.displayImage = ResourceManager.getInstance().getImg("displayImage", "Prop", "FireCDBuff");
        this.price = 5;
        this.buffValue =  5;
    }

    @Override
    public void updateValue(BasePlayer player) {
        BaseWeapon weapon = player.getWeapon();
        if (weapon != null) {
            weapon.getBulletSpeed().set(weapon.getBulletSpeed().get()+buffValue);
        }
    }
}
