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

    public  <T, V extends Enum<V>>  ObjectPool<T, V> getObjectPool(String poolName, ResourceFactory<T, V> resourceFactory, V v, int initialSize, int maxSize) {

        if (pools.containsKey(poolName)) {
            return (ObjectPool<T, V>) pools.get(poolName);
        }
        ObjectPool<T, V> objectPool = ObjectPool.createPool(resourceFactory, v, initialSize, maxSize);
        pools.put(poolName, objectPool);
        return objectPool;
    }

}

