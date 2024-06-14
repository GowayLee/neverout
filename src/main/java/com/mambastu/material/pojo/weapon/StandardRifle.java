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

import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class StandardRifle extends BaseWeapon{

    public StandardRifle() {
        damage = 8;
        bulletSpeed = 20;
        range = 600;
        coolTime = 70;
        bulletType = BulletType.StandardBullet;
        coolStatus = Status.READY;
        coolTimer.setDuration(Duration.millis(coolTime));
        coolTimer.setOnFinished(event ->{
            setStatus(Status.READY);
            coolTimer.stop(); // 停止冷却计时器。
        });
    }

    @Override
    public BaseBullet fire(double x, double y, LinkedList<BaseMonster> monsters, Set<GameInput> activeInputs, Pane root) {
        if (activeInputs.contains(GameInput.FIRE) && coolStatus == Status.READY && monsters.size() > 0){
            try {
                BaseBullet newBullet = BulletFactory.getInstance().create(bulletType);
                newBullet.setProps(damage, bulletSpeed, range);
                newBullet.setTarget(selectTarget(x, y, monsters));
                newBullet.setPos(x, y);
                newBullet.putOnPane(root);
                setStatus(Status.COOLDOWN);
                coolTimer.play(); // 开始冷却计时器。
                return newBullet;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public BaseEntity selectTarget(double x, double y, LinkedList<BaseMonster> monsters) { // 选择目标，并传入当前武器的位置信息。TODO: 改进算法
        List<Double> distList = new ArrayList<>();
        for (BaseEntity entity : monsters) {
            distList.add((entity.getX().get() - x) * (entity.getX().get() - x) + (entity.getY().get() - y) * (entity.getY().get() - y)); // 计算距离，并平方。
        }
        double minDist = distList.get(0); // 初始化最小距离。
        int minIndex = 0; // 初始化最小距离的索引。
        for (int i = 1; i < distList.size(); i++) { // 遍历所有距离，找到最小距离。
            if (distList.get(i) < minDist) { // 如果当前距离小于最小距离。
                minDist = distList.get(i); // 更新最小距离。
                minIndex = i; // 更新最小距离的索引。
            }
        }
        return monsters.get(minIndex); // 返回最小距离的实体。
    }

}
