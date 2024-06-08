package com.mambastu;

import com.mambastu.material.resource.ResourceManager;

import static com.mambastu.material.factories.MonsterFactory.createMonster;
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
        createMonster(BossMonster);

    }
}
