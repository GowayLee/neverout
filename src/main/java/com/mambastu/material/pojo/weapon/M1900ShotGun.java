package com.mambastu.material.pojo.weapon;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.mambastu.controller.input.comp.GameInput;
import com.mambastu.factories.BulletFactory;
import com.mambastu.material.pojo.entity.BaseEntity;
import com.mambastu.material.pojo.entity.bullet.BaseBullet;
import com.mambastu.material.pojo.entity.bullet.BulletType;
import com.mambastu.material.pojo.entity.monster.BaseMonster;
import com.mambastu.util.GlobalVar;

import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class M1900ShotGun extends BaseWeapon {

    private final PauseTransition shotTimer = new PauseTransition();
    private int fireCount;
    private final double[] offsetArr = { -0.23, -0.15, -0.07 , 0.0, 0.07, 0.15, 0.23 };
    private ShotState shotState;
    private enum ShotState { SHOOTING, READY };

    public M1900ShotGun() {
        super.damage.set(20);
        super.bulletSpeed.set(13);
        super.range.set(450);
        super.coolTime.set(1100 );
        super.bulletType = BulletType.NailBullet;
        super.coolStatus = Status.READY;
        this.shotState = ShotState.READY;
        super.coolTimer.setDuration(Duration.millis(coolTime.get()));
        super.coolTimer.setOnFinished(event -> {
            coolStatus = Status.READY;
            coolTimer.stop(); // 停止冷却计时器。
        });
        this.shotTimer.setDuration(Duration.millis(100));
        this.shotTimer.setOnFinished(event -> {
            shotState = ShotState.READY;
        });
        this.fireCount = 0;
    }

    @Override
    public List<BaseBullet> fire(double x, double y, LinkedList<BaseMonster> monsters, Set<GameInput> activeInputs) {
        if (activeInputs.contains(GameInput.FIRE) && coolStatus == Status.READY && shotState == ShotState.READY && monsters.size() > 0) {
            newBulletList.clear();
            for (int i = 0; i < 7; i++) {
                try {
                    BaseBullet newBullet = BulletFactory.getInstance().create(bulletType);
                    newBullet.setProps(damage.get(), bulletSpeed.get(), range.get());
                    newBullet.setPos(x, y);
                    newBullet.setTarget(selectTarget(x, y, monsters), offsetArr[i]);
                    newBullet.putOnPane(GlobalVar.getGamePane());
                    newBulletList.add(newBullet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            fireCount++;
            if (fireCount >= 2) { // 连续发射2发子弹后，进入冷却状态。
                coolStatus = Status.COOLDOWN;
                coolTimer.play(); // 开始冷却计时器。
                fireCount = 0;
            }
            shotState = ShotState.SHOOTING;
            shotTimer.play();
            return newBulletList;
        }
        return null;
    }

    @Override
    public BaseEntity selectTarget(double x, double y, LinkedList<BaseMonster> monsterList) { // 选择目标，并传入当前武器的位置信息。TODO:
                                                                                              // 改进算法
        List<Double> distList = new ArrayList<>();
        for (BaseEntity entity : monsterList) {
            distList.add((entity.getX().get() - x) * (entity.getX().get() - x)
                    + (entity.getY().get() - y) * (entity.getY().get() - y)); // 计算距离，并平方。
        }
        double minDist = distList.get(0); // 初始化最小距离。
        int minIndex = 0; // 初始化最小距离的索引。
        for (int i = 1; i < distList.size(); i++) { // 遍历所有距离，找到最小距离。
            if (distList.get(i) < minDist) { // 如果当前距离小于最小距离。
                minDist = distList.get(i); // 更新最小距离。
                minIndex = i; // 更新最小距离的索引。
            }
        }
        return monsterList.get(minIndex); // 返回最小距离的实体。
    }
}
