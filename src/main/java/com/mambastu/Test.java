package com.mambastu;

import com.mambastu.material.factories.MonsterFactory;
import com.mambastu.material.pojo.entity.monster.BaseMonster;
import com.mambastu.material.pojo.entity.monster.BossMonster;
import com.mambastu.material.pojo.entity.monster.MonsterTypes;
import com.mambastu.material.pools.ObjectPool;
import com.mambastu.material.pools.ObjectPoolManager;
import com.mambastu.material.resource.ResourceManager;


import java.util.Arrays;

import static com.mambastu.material.pojo.entity.monster.MonsterTypes.BossMonster;

import static com.mambastu.material.resource.ResourceManager.getResourceManager;

/**
 * Author:JngyEn
 * Description:
 * DateTime: 2024/6/8下午7:45
 **/
public class Test {
    public static void main(String[] args) {
        ResourceManager a = getResourceManager();
        a.loadResources();
        MonsterFactory monsterFactory = MonsterFactory.getMonsterFactory();
        ObjectPoolManager objectPoolManager = ObjectPoolManager.getObjectPoolManagerInstance();
        ObjectPool<BaseMonster, MonsterTypes> monsterPool = objectPoolManager.getObjectPool("monsterPool",monsterFactory, BossMonster,20,50);
        BossMonster bossA = (com.mambastu.material.pojo.entity.monster.BossMonster) monsterPool.borrowObject();
        System.out.println(bossA.toString());
    }
}
