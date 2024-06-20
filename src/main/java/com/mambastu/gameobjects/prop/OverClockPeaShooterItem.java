package com.mambastu.gameobjects.prop;

import com.mambastu.enums.gameobjects.BulletType;
import com.mambastu.gameobjects.entity.player.BasePlayer;
import com.mambastu.gameobjects.weapon.BaseWeapon;
import com.mambastu.gameobjects.weapon.PeaShooter;
import com.mambastu.resource.media.impl.ImageManager;

public class OverClockPeaShooterItem extends BaseProp {

    public OverClockPeaShooterItem() {
        this.displayImage = ImageManager.getInstance().getImg("displayImage", "Prop", "OverClockPeaShooterItem");
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
            damageBuff = oldWeapon.getDamageBuff();
            bulletSpeedBuff = oldWeapon.getBulletSpeedBuff();
            coolTimeBuff = oldWeapon.getCoolTimeBuff();
            rangeBuff = oldWeapon.getRangeBuff();
            temp = player.getWeapon().getBulletType();
        }

        player.setWeapon(new PeaShooter());
        BaseWeapon newWeapon = player.getWeapon();
        newWeapon.updateBuffProperties(damageBuff, bulletSpeedBuff, coolTimeBuff, rangeBuff);
        newWeapon.updateValueProperties(18, 10, 120, 400, temp);
    }
}
