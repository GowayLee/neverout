package com.mambastu.utils.pool;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mambastu.gameobjects.entity.BaseEntity;

public class ObjectPoolManager {
    private static final Logger logger = LogManager.getLogger(ObjectPoolManager.class);

    private static final ObjectPoolManager INSTANCE = new ObjectPoolManager();

    private HashMap<Class<? extends BaseEntity>, ObjectPool<? extends BaseEntity>> pools = new HashMap<>();

    private ObjectPoolManager() {
    }

    
    /** 
     * @return ObjectPoolManager
     */
    public static ObjectPoolManager getInstance() {
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    public  <T extends BaseEntity> ObjectPool<T> getObjectPool(Class<T> objectClass) {
        if (!pools.containsKey(objectClass)) { // 如果不存在该池，则新建一个
            createObjectPool(objectClass, 20);
        }
        return (ObjectPool<T>) pools.get(objectClass);
    }

    /**
     * Create an object pool depend on class type of the target object.
     * 
     * @param <T>
     * @param objectClass
     * @param maxSize
     * @return
     */
    private <T extends BaseEntity> ObjectPool<T> createObjectPool(Class<? extends BaseEntity> objectClass, int maxSize) { 
        @SuppressWarnings({ "unchecked", "rawtypes" })
        ObjectPool<T> objectPool = new ObjectPool(objectClass, maxSize);
        pools.put(objectClass, objectPool);
        logger.info("Create a new object pool for " + objectClass.getName());
        return objectPool;
    }

    public void close() { // 抛弃所有池的引用, 进行垃圾回收
        pools.clear();
        System.gc();
    }

}
