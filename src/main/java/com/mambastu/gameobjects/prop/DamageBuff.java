package com.mambastu.gameobjects.prop;

import com.mambastu.gameobjects.entity.player.BasePlayer;
import com.mambastu.gameobjects.weapon.BaseWeapon;
import com.mambastu.resource.media.impl.ImageManager;

public class DamageBuff extends BaseProp {

    public DamageBuff() {
        this.displayImage = ImageManager.getInstance().getImg("displayImage", "Prop", "DamageBuff");
        this.price = 10;
        this.buffValue = 0.20;
    }

    @Override
    public void updateValue(BasePlayer player) {
        BaseWeapon weapon = player.getWeapon();
        if (weapon != null) {
            weapon.getDamage().set((int) (weapon.getDamage().get() * (buffValue + 1.0))); // 增加攻击力
            weapon.setDamageBuff(weapon.getDamageBuff() + buffValue);
        }
    }

}
