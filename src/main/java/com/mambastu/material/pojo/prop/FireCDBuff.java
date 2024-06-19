package com.mambastu.material.pojo.prop;

import com.mambastu.material.pojo.entity.player.BasePlayer;
import com.mambastu.material.pojo.weapon.BaseWeapon;
import com.mambastu.material.resource.ResourceManager;

public class FireCDBuff extends BaseProp{

    public FireCDBuff() {
        this.displayImage = ResourceManager.getInstance().getImg("displayImage", "Prop", "FireCDBuff");
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
