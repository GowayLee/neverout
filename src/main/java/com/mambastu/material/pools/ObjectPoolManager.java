package com.mambastu.material.pools;

import com.mambastu.material.factories.ResourceFactory;

import java.util.HashMap;

/**
 * Author:JngyEn
 * Description:
 * DateTime: 2024/6/9上午11:35
 **/
public class ObjectPoolManager {
    private  HashMap<String, ObjectPool<?, ?>> pools = new HashMap<>();
    private static ObjectPoolManager objectPoolManagerInstance;

    private ObjectPoolManager() {
    }

    public static ObjectPoolManager getObjectPoolManagerInstance (){
        if (objectPoolManagerInstance == null) {
            objectPoolManagerInstance = new ObjectPoolManager();
        }
        return objectPoolManagerInstance;
    }

    /**通过获得的池管理器来取得对象池以及防止重复产生某一个对象的对象池
     *
     * @param poolName :命名规则：类名 + Pool
     * @param resourceFactory
     * @param v
     * @param initialSize
     * @param maxSize
     * @return : 返回某个子类的ObjectPool对象
     * @param <T>
     * @param <V>
     */
    public  <T, V extends Enum<V>>  ObjectPool<T, V> getObjectPool(String poolName, ResourceFactory<T, V> resourceFactory, V v, int initialSize, int maxSize) {

        if (pools.containsKey(poolName)) {
            return (ObjectPool<T, V>) pools.get(poolName);
        }
        ObjectPool<T, V> objectPool = ObjectPool.createPool(resourceFactory, v, initialSize, maxSize);
        pools.put(poolName, objectPool);
        return objectPool;
    }

}

