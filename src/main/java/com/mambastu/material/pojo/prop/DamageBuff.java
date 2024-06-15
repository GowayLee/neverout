package com.mambastu.material.pojo.prop;

import com.mambastu.material.pojo.entity.player.BasePlayer;
import com.mambastu.material.pojo.weapon.BaseWeapon;
import com.mambastu.material.resource.ResourceManager;

public class DamageBuff extends BaseProp {

    public DamageBuff() {
        this.displayImage = ResourceManager.getInstance().getImg("displayImage", "Prop", "DamageBuff");
        this.price = 5;
        this.buffValue = 10.0;
    }

    @Override
    public void updateValue(BasePlayer player) {
        BaseWeapon weapon = player.getWeapon();
        if (weapon != null) {
            weapon.getDamage().set(weapon.getDamage().get() + (int) buffValue); // 增加攻击力
        }
    }

}
