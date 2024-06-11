package com.mambastu.factories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mambastu.material.pojo.entity.player.BasePlayer;
import com.mambastu.material.pojo.entity.player.LaughPlayer;
import com.mambastu.material.pojo.entity.player.PlayerTypes;
import com.mambastu.material.pools.ObjectPoolManager;

public class PlayerFactory implements EntityFactory<BasePlayer, PlayerTypes>  {
    private static final Logger logger = LogManager.getLogger(PlayerFactory.class);

    private static PlayerFactory INSTANCE = new PlayerFactory();;

    // private final ObjectPoolManager objectPoolManager = ObjectPoolManager.getInstance();

    private PlayerFactory() {
    }

    public static PlayerFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public BasePlayer create(PlayerTypes playerTypes) { // 暂时不接入对象池
        switch (playerTypes) {
            case LaughPlayer:
                try {
                    LaughPlayer laughPlayer = (LaughPlayer) LaughPlayer.class.getDeclaredConstructor().newInstance();
                    laughPlayer.init();
                    return laughPlayer;
                } catch (Exception e) {
                    logger.error("Error in creating Monster! Unknown monster type: " + playerTypes);
                    e.printStackTrace();
                }
            default:
                logger.error("Error in creating Monster! Unknown monster type: " + playerTypes);
                throw new IllegalArgumentException("Unknown player type");
        }
    }

    @Override
    public void delete(BasePlayer obj) {
        System.gc(); // 删除对象的情况多出现于关卡结束，建议系统进行垃圾回收
    }
}
