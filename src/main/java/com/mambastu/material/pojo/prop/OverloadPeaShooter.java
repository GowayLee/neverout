package com.mambastu.material.pojo.prop;

import com.mambastu.material.pojo.entity.bullet.BulletType;
import com.mambastu.material.pojo.entity.player.BasePlayer;
import com.mambastu.material.pojo.weapon.BaseWeapon;
import com.mambastu.material.pojo.weapon.PeaShooter;
import com.mambastu.material.resource.ResourceManager;

public class OverloadPeaShooter extends BaseProp{
    public OverloadPeaShooter() {
        this.displayImage = ResourceManager.getInstance().getImg("displayImage", "Prop", "OverloadPeaShooterItem");
        this.price = 50;
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
        newWeapon.setBulletType(temp);
        newWeapon.getDamage().set((int) (120 * damageBuff));
        newWeapon.getBulletSpeed().set(20 * bulletSpeedBuff);
        newWeapon.getRange().set(850 * rangeBuff);
        newWeapon.getCoolTime().set(700 * coolTimeBuff);
    }
}
