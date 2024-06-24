package com.mambastu.factories;

import com.mambastu.enums.gameobjects.MonsterTypes;
import com.mambastu.gameobjects.entity.monster.*;
import com.mambastu.utils.pool.ObjectPool;
import com.mambastu.utils.pool.ObjectPoolManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MonsterFactory implements EntityFactory<BaseMonster, MonsterTypes> {
    private static final Logger logger = LogManager.getLogger(MonsterFactory.class);

    private static MonsterFactory INSTANCE = new MonsterFactory();;

    private final ObjectPoolManager objectPoolManager = ObjectPoolManager.getInstance();

    private MonsterFactory() {
    }

    public static MonsterFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Get an instance from the relative object pool. If the object pool is empty, create a new instance.
     *
     * @param monsterType
     * @return BaseMonster
     */
    @Override
    public BaseMonster create(MonsterTypes monsterType) {
        switch (monsterType) {
            case BossMonster:
                BossMonster bossMonster = (BossMonster) objectPoolManager.getObjectPool(BossMonster.class).borrowObject();
                bossMonster.setOnStage(true);
                bossMonster.init();
                return bossMonster;
            case HotMonster:
                HotMonster hotMonster = (HotMonster) objectPoolManager.getObjectPool(HotMonster.class).borrowObject();
                hotMonster.setOnStage(true);
                hotMonster.init();
                return hotMonster;
            case HellLordMonster:
                HellLordMonster hellLordMonster = (HellLordMonster) objectPoolManager.getObjectPool(HellLordMonster.class).borrowObject();
                hellLordMonster.setOnStage(true);
                hellLordMonster.init();
                return hellLordMonster;
            default:
                logger.error("Error in creating Monster! Unknown monster type: " + monsterType);
                throw new IllegalArgumentException("Unknown monster type");
        }
    }

    /**
     * Return an instance to the relative object pool.
     * 
     * @param monsterType
     */
    @Override
    public void delete(BaseMonster obj) {
        obj.setOnStage(false);
        @SuppressWarnings("unchecked")
        ObjectPool<BaseMonster> objectPool = (ObjectPool<BaseMonster>)  ObjectPoolManager.getInstance().getObjectPool(obj.getClass());
        objectPool.returnObject(obj);
    }
}
