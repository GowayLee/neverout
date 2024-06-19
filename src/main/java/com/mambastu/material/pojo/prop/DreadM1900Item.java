package com.mambastu.material.pojo.prop;

import com.mambastu.material.pojo.entity.bullet.BulletType;
import com.mambastu.material.pojo.entity.player.BasePlayer;
import com.mambastu.material.pojo.weapon.BaseWeapon;
import com.mambastu.material.pojo.weapon.M1900ShotGun;
import com.mambastu.material.resource.ResourceManager;

public class DreadM1900Item extends BaseProp {
    public DreadM1900Item() {
        this.displayImage = ResourceManager.getInstance().getImg("displayImage", "Prop", "DreadM1900Item");
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
        player.getWeapon().setBulletType(temp);
        player.setWeapon(new M1900ShotGun());
        BaseWeapon weapon = player.getWeapon();
        weapon.getBulletSpeed().set(15 * bulletSpeedBuff);
        weapon.getRange().set(450 * rangeBuff);
        weapon.getCoolTime().set(1600 * coolTimeBuff);
        weapon.getDamage().set((int) (34 * damageBuff));
    }
}
