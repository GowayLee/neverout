package com.mambastu.factories;


/**
 * Author:JngyEn
 * Description: 工厂接口
 * DateTime: 2024/6/9上午10:35
 **/
public interface EntityFactory<T, V extends Enum<V>> {
    /**
     * Borrow an instance from the embeded pool. If there is no instance available, a new one will be created.
     * 
     * @param type
     * @return an instance of the entity.
     */
    T create(V type);

    /**
     * Return an instance to the embeded pool
     *
     * @param obj the instance to return.
     */
    void delete(T obj);
}