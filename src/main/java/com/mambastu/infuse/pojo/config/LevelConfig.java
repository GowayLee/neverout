package com.mambastu.infuse.pojo.config;

import java.util.HashMap;
import java.util.Map;

import com.mambastu.material.pojo.entity.monster.BaseMonster;
import com.mambastu.material.pojo.entity.monster.BossMonster;
import com.mambastu.material.pojo.entity.monster.HotMonster;
import com.mambastu.material.pojo.entity.player.BasePlayer;
import com.mambastu.material.pojo.entity.player.Player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LevelConfig {
    // Listing features of the level
    private int levelNum;

    private double monsterScalNum = 2000;
    private double monsterScalSpeed;
    private double monsterScalHP;
    private double monsterScalDamage;
    private double monsterScalGold;

    private Class<? extends BasePlayer> playerEgg;
    private Map<Class<? extends BaseMonster>, Double> monsterEggList;

    public LevelConfig() {
        monsterEggList = new HashMap<>();
        monsterEggList.put(HotMonster.class, 1.0);
        monsterEggList.put(BossMonster.class, 2.0);
        playerEgg = Player.class;
    }

}
