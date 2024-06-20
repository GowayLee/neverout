package com.mambastu.utils.pool;

import java.util.ArrayDeque;
import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mambastu.gameobjects.entity.BaseEntity;

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

    private final Queue<T> pool;
    private final Class<T> objectClass;
    private double maxCapacity;
    private double curCapacity;
    private boolean readyForEnlarge = true; // 是否可以扩容(线程安全)

    public ObjectPool(Class<T> objectClass, int maxCapacity) {
        this.pool = new ArrayDeque<>();
        this.objectClass = objectClass;
        this.maxCapacity = maxCapacity;
        this.curCapacity = maxCapacity; // 当前池中的可使用对象数量
        init();
    }

    private void init() { // 初始化对象池
        for (int i = 0; i < maxCapacity; i++) {
            try {
                pool.add(objectClass.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                logger.error("Error in initializing object pool!");
                e.printStackTrace();
            }
        }
    }

    public T borrowObject() {
        if (!pool.isEmpty()) { // 如果池中有元素，直接返回
            if (curCapacity / maxCapacity < 0.3 && readyForEnlarge) { // 如果池中元素数量小于总容量的30%，扩容
                readyForEnlarge = false;
                enlargePool();
            }
            curCapacity--;
            return pool.poll();
        } else { // 如果池中没有元素，立刻创建新元素并返回，同时增加池的容量
            try {
                pool.add(objectClass.getDeclaredConstructor().newInstance());
                maxCapacity++;
                logger.warn("Object pool reaches upper limit!");
                return pool.poll();
            } catch (Exception e) {
                logger.error("Error in enlarging object pool!");
                e.printStackTrace();
            }
        }
        return null; // 如果创建新元素失败，抛出异常返回null
    }

    public void returnObject(T obj) {
        pool.add(obj);
        curCapacity++;
        // logger.info("Return object to pool! ");
    }

    private void enlargePool() {
        new Thread(() -> { // 异步扩容，避免阻塞主线程
            for (int i = 0; i < maxCapacity; i++) {
                try {
                    pool.add(objectClass.getDeclaredConstructor().newInstance());
                } catch (Exception e) {
                    logger.error("Error in enlarging object pool!");
                    e.printStackTrace();
                }
            }
            readyForEnlarge = true;
            curCapacity += maxCapacity; // 扩容后，池中元素数量也增加
            maxCapacity += maxCapacity;
            logger.info("Enlarge the object pool for " + objectClass.getSimpleName() + "! Current Capacity: "
                    + maxCapacity);
        }).start();
    }
}
