package com.mambastu.gameobjects.prop;

import com.mambastu.enums.gameobjects.BulletType;
import com.mambastu.gameobjects.entity.player.BasePlayer;
import com.mambastu.gameobjects.weapon.BaseWeapon;
import com.mambastu.gameobjects.weapon.M1900ShotGun;
import com.mambastu.resource.media.impl.ImageManager;

public class DreadM1900Item extends BaseProp {
    public DreadM1900Item() {
        this.displayImage = ImageManager.getInstance().getImg("displayImage", "Prop", "DreadM1900Item");
        this.price = 60;
        this.buffValue = 0;
    }

    @Override
    public void updateValue(BasePlayer player) {
        BaseWeapon oldWeapon = player.getWeapon();
        double damageBuff = 1.0;
        double bulletSpeedBuff = 1.0;
        double coolTimeBuff = 1.0;
        double rangeBuff = 1.0;
        BulletType temp = BulletType.StandardBullet;
        if (oldWeapon != null) {
            temp = player.getWeapon().getBulletType();
            damageBuff = oldWeapon.getDamageBuff();
            bulletSpeedBuff = oldWeapon.getBulletSpeedBuff();
            coolTimeBuff = oldWeapon.getCoolTimeBuff();
            rangeBuff = oldWeapon.getRangeBuff();
        }

        player.setWeapon(new M1900ShotGun());
        BaseWeapon newWeapon = player.getWeapon();
        newWeapon.updateBuffProperties(damageBuff, bulletSpeedBuff, coolTimeBuff, rangeBuff);
        newWeapon.updateValueProperties(34, 15, 1600, 450, temp);
    }
}
