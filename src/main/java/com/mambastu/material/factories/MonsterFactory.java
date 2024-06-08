package com.mambastu.material.factories;

import com.mambastu.material.pojo.entity.monster.BaseMonster;
import com.mambastu.material.pojo.entity.monster.BossMonster;
import com.mambastu.material.pojo.entity.monster.HotMonster;
import com.mambastu.material.pojo.entity.monster.MonsterTypes;
import com.mambastu.material.resource.ResourceManager;

/**
 * Author:JngyEn
 * Description: 这是实例化怪物的工厂，负责从资源管理器中挑选url来实例化怪物对象
 * DateTime: 2024/6/8下午12:27
 **/
public class MonsterFactory {
    public static BaseMonster createMonster (MonsterTypes monsterType) {
        ResourceManager resourceManager = ResourceManager.getResourceManager();
        switch (monsterType) {
            case BossMonster:
                String imageUrl = resourceManager.getResourcesByTypeAndCase("bornImage","Monster","BossMonster");
                System.out.println("BossMonster imageUrl:" + imageUrl);
                return new BossMonster(imageUrl);
            case HotMonster:
                return new HotMonster("/static/image/fever.png");
            default:
                throw new IllegalArgumentException("Unknown monster type");
        }
    }
}
