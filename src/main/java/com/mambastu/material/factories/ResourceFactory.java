package com.mambastu.material.factories;

import com.mambastu.material.pojo.entity.monster.BaseMonster;
import com.mambastu.material.pojo.entity.monster.MonsterTypes;

/**
 * Author:JngyEn
 * Description: 工厂接口
 * DateTime: 2024/6/9上午10:35
 **/
public interface ResourceFactory<T, V extends Enum<V>> {
    T create(V type);
}