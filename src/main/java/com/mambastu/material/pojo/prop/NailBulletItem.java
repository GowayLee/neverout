package com.mambastu.material.pojo.prop;

import com.mambastu.material.pojo.entity.bullet.BulletType;
import com.mambastu.material.pojo.entity.player.BasePlayer;
import com.mambastu.material.pojo.weapon.BaseWeapon;
import com.mambastu.material.resource.ResourceManager;

public class NailBulletItem extends BaseProp{
    public NailBulletItem() {
        this.displayImage = ResourceManager.getInstance().getImg("displayImage", "Prop", "NailBulletItem");
        this.price = 30;
        this.buffValue = 0;
    }

    @Override
    public void updateValue(BasePlayer player) {
        BaseWeapon weapon = player.getWeapon();
        if (weapon != null) {
            weapon.setBulletType(BulletType.NailBullet);
        }
    }
}
