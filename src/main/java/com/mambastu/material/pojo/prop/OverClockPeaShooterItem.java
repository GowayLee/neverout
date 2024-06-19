package com.mambastu.material.pojo.prop;

import com.mambastu.material.pojo.entity.bullet.BulletType;
import com.mambastu.material.pojo.entity.player.BasePlayer;
import com.mambastu.material.pojo.weapon.BaseWeapon;
import com.mambastu.material.pojo.weapon.PeaShooter;
import com.mambastu.material.resource.ResourceManager;

public class OverClockPeaShooterItem extends BaseProp {

    public OverClockPeaShooterItem() {
        this.displayImage = ResourceManager.getInstance().getImg("displayImage", "Prop", "OverClockPeaShooterItem");
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
        newWeapon.setBulletType(temp);
        newWeapon.getBulletSpeed().set(10 * bulletSpeedBuff);
        newWeapon.getRange().set(400 * rangeBuff);
        newWeapon.getCoolTime().set(100 * coolTimeBuff);
        newWeapon.getDamage().set((int) (18 * damageBuff));
    }
}
