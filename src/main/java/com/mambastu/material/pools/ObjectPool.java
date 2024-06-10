package com.mambastu.material.pools;

import com.mambastu.material.pojo.entity.BaseEntity;

import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Author:JngyEn
 * Description: 一个子类一个对象池
 * 如果怪物类都放在一个对象池中，那么这个池中的类型是baseMonster，后面无法转换出相应的类型
 * @ T:某个pojo.entity 的基类
 * @ V:某个pojo.entity枚举类
 * DateTime: 2024/6/9上午10:45
 **/
public class ObjectPool<T extends BaseEntity> {
    private static final Logger logger = LogManager.getLogger(ObjectPool.class);

    private final Stack<T> pool;
    private final Class<T> objectClass;
    private int maxCapacity;

    /**
     *
     * @param resourceFactory : 该元素对应的工厂类
     * @param v               : 枚举类包含的一个元素,同时也是这个线程池的具体生成类型
     * @param initCapacity
     * @param maxCapacity
     */
    public ObjectPool(Class<T> objectClass, int maxCapacity) {
        this.pool = new Stack<>();
        this.objectClass = objectClass;
        this.maxCapacity = maxCapacity;
        init();
    }

    private void init() { // 初始化对象池
        for (int i = 0; i < maxCapacity; i++) {
            try {
                pool.push(objectClass.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                logger.error("Error in initializing object pool!");
                e.printStackTrace();
            }

        }
    }

    // public static <T, V extends Enum<V>> ObjectPool<T, V> createPool(ResourceFactory<T, V> resourceFactory, V v,
    //         int initCapacity, int maxCapacity) {
    //     return new ObjectPool<>(resourceFactory, v, initCapacity, maxCapacity);
    // }

    /**
     * 当池中元素不够时自动扩容
     *
     * @return : 返回的超类
     */
    public T borrowObject() {
        if (!pool.isEmpty()) { // 如果池中有元素，直接返回
            return pool.pop();
        } else { // 如果池中没有元素，创建新元素并返回，同时增加池的容量 TODO: 考虑增加扩容机制
            try {
                T obj = objectClass.getDeclaredConstructor().newInstance();
                maxCapacity++;
                logger.info("Enlarge obejct pool! Current Capacity: " + maxCapacity);
                return obj;
            } catch (Exception e) {
                logger.error("Error in enlarging object pool!");
                e.printStackTrace();
            }
        }
        return null; // 如果创建新元素失败，抛出异常返回null
    }

    public void returnObject(T obj) {
        pool.push(obj);
        logger.info("Return object to pool! ");
    }

}
