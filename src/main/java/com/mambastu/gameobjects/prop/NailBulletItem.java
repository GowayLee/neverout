package com.mambastu.gameobjects.prop;

import com.mambastu.enums.gameobjects.BulletType;
import com.mambastu.gameobjects.entity.player.BasePlayer;
import com.mambastu.gameobjects.weapon.BaseWeapon;
import com.mambastu.resource.media.impl.ImageManager;

public class NailBulletItem extends BaseProp{
    public NailBulletItem() {
        this.displayImage = ImageManager.getInstance().getImg("displayImage", "Prop", "NailBulletItem");
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
