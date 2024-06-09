package com.mambastu.material.pools;
import com.mambastu.material.factories.ResourceFactory;

import java.util.Stack;
/**
 * Author:JngyEn
 * Description: 一个子类一个对象池
 *              如果怪物类都放在一个对象池中，那么这个池中的类型是baseMonster，后面无法转换出相应的类型
 * DateTime: 2024/6/9上午10:45
 **/
public class ObjectPool<T, V extends Enum<V>> {
    private Stack<T> pool;
    private int initialSize;
    private int currentSize;
    private int maxSize;
    private V enumV;
    private ResourceFactory<T, V> resourceFactory;

    /**
     *
     * @param resourceFactory : 该元素对应的工厂类
     * @param v : 枚举类包含的一个元素
     * @param initialSize
     * @param maxSize
     */
    private ObjectPool(ResourceFactory<T, V> resourceFactory, V v, int initialSize, int maxSize) {
        this.resourceFactory = resourceFactory;
        this.initialSize = initialSize;
        this.maxSize = maxSize;
        this.enumV = v;
        this.pool = new Stack<>();
        initializePool();
    }

    private void initializePool() {
        for (int i = 0; i < initialSize; i++) {
            pool.push(resourceFactory.create(enumV));
            currentSize++;
        }
    }

    public static <T, V extends Enum<V>> ObjectPool<T,V> createPool (ResourceFactory<T, V> resourceFactory,V v,int initialSize, int maxSize) {
            return new ObjectPool<>(resourceFactory,v, initialSize, maxSize );
    }

    /**
     * 当池中元素不够时自动扩容
     *
     * @return : 返回的泛型类，一定记得强制转化
     */
    public  T borrowObject() {
        if (pool.isEmpty() && currentSize < maxSize) {
            T obj = resourceFactory.create(enumV);
            currentSize++;
            pool.push(obj);
            return obj;
        } else if (!pool.isEmpty()) {
            return pool.pop();
        }
        return null;
    }

    public synchronized void returnObject(T obj) {
        if (pool.size() < maxSize) {
            pool.push(obj);
        }
    }

}
