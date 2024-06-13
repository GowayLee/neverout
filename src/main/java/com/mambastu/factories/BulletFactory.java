package com.mambastu.factories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mambastu.material.pojo.entity.bullet.BaseBullet;
import com.mambastu.material.pojo.entity.bullet.BulletType;
import com.mambastu.material.pojo.entity.bullet.StandardBullet;
import com.mambastu.material.pools.ObjectPool;
import com.mambastu.material.pools.ObjectPoolManager;

public class BulletFactory implements EntityFactory<BaseBullet, BulletType>{
private static final Logger logger = LogManager.getLogger(BulletFactory.class);

    private static BulletFactory INSTANCE = new BulletFactory();;

    private final ObjectPoolManager objectPoolManager = ObjectPoolManager.getInstance();

    private BulletFactory() {
    }

    public static BulletFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public BaseBullet create(BulletType bulletType) {
        switch (bulletType) {
            case StandardBullet:
                StandardBullet standardBullet = (StandardBullet) objectPoolManager.getObjectPool(StandardBullet.class).borrowObject();
                standardBullet.init();
                return standardBullet;

            default:
                logger.error("Error in creating Monster! Unknown monster type: " + bulletType);
                throw new IllegalArgumentException("Unknown monster type");
        }
    }

    @Override
    public void delete(BaseBullet obj) {
        @SuppressWarnings("unchecked")
        ObjectPool<BaseBullet> objectPool = (ObjectPool<BaseBullet>)  ObjectPoolManager.getInstance().getObjectPool(obj.getClass());
        objectPool.returnObject(obj);
    }
}
