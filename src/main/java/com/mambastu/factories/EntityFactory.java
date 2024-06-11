package com.mambastu.factories;


/**
 * Author:JngyEn
 * Description: 工厂接口
 * DateTime: 2024/6/9上午10:35
 **/
public interface EntityFactory<T, V extends Enum<V>> {
    T create(V type);

    void delete(T obj);
}