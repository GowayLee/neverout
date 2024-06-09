package com.mambastu.material.factories;

import com.mambastu.material.pojo.entity.monster.BaseMonster;
import com.mambastu.material.pojo.entity.monster.BossMonster;
import com.mambastu.material.pojo.entity.monster.HotMonster;
import com.mambastu.material.pojo.entity.monster.MonsterTypes;
import com.mambastu.material.resource.ResourceManager;

public class MonsterFactory implements ResourceFactory<BaseMonster, MonsterTypes> {
    private static MonsterFactory monsterFactory;

    private MonsterFactory() {}

    public static MonsterFactory getMonsterFactory() {
        if (monsterFactory == null) {
            monsterFactory = new MonsterFactory();
        }
        return monsterFactory;
    }

    @Override
    public  BaseMonster  create(MonsterTypes monsterType) {
        ResourceManager resourceManager = ResourceManager.getResourceManager();
        switch (monsterType) {
            case BossMonster:
                String imageUrl = resourceManager.getResourcesByTypeAndCase("bornImage", "Monster", "BossMonster");
                System.out.println("BossMonster imageUrl:" + imageUrl);
                return  new BossMonster(imageUrl);
            case HotMonster:
                return new HotMonster("/static/image/fever.png");
            default:
                throw new IllegalArgumentException("Unknown monster type");
        }
    }
}
