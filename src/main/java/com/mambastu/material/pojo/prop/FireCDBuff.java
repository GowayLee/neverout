package com.mambastu.material.pojo.prop;

import com.mambastu.material.pojo.entity.player.BasePlayer;
import com.mambastu.material.pojo.weapon.BaseWeapon;
import com.mambastu.material.resource.ResourceManager;

public class FireCDBuff extends BaseProp{

    public FireCDBuff() {
        this.displayImage = ResourceManager.getInstance().getImg("displayImage", "Prop", "FireCDBuff");
        this.price = 5;
        this.buffValue = 0.8;
    }

    @Override
    public void updateValue(BasePlayer player) {
        BaseWeapon weapon = player.getWeapon();
        if (weapon != null) {
            weapon.getCoolTime().set(weapon.getCoolTime().get() > 20 ? weapon.getCoolTime().get() * buffValue : weapon.getCoolTime().get()); // 增加攻击力, 下限为20ms
        }
    }
}
