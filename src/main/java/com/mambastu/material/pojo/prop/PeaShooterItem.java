package com.mambastu.material.pojo.prop;

import com.mambastu.material.pojo.entity.bullet.BulletType;
import com.mambastu.material.pojo.entity.player.BasePlayer;
import com.mambastu.material.pojo.weapon.BaseWeapon;
import com.mambastu.material.pojo.weapon.PeaShooter;
import com.mambastu.material.resource.ResourceManager;

public class PeaShooterItem extends BaseProp {
    public PeaShooterItem() {
        this.displayImage = ResourceManager.getInstance().getImg("displayImage", "Prop", "PeaShooterItem");
        this.price = 15;
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
        if (oldWeapon != null) { // 如果玩家已经拥有武器，则将当前武器的属性加成应用到新武器上，并保留子弹类型不变。
            damageBuff = oldWeapon.getDamageBuff();
            bulletSpeedBuff = oldWeapon.getBulletSpeedBuff();
            coolTimeBuff = oldWeapon.getCoolTimeBuff();
            rangeBuff = oldWeapon.getRangeBuff();
            temp = player.getWeapon().getBulletType();
        }

        player.setWeapon(new PeaShooter());
        BaseWeapon newWeapon = player.getWeapon();
        newWeapon.updateBuffProperties(damageBuff, bulletSpeedBuff, coolTimeBuff, rangeBuff);
        newWeapon.updateValueProperties(34, 3, 300, 700, temp);
    }
}
