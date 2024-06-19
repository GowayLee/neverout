package com.mambastu.controller.level.context.dto.config;

import java.util.ArrayList;
import java.util.List;

import com.mambastu.material.pojo.entity.monster.MonsterTypes;
import com.mambastu.material.pojo.entity.player.BasePlayer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LevelConfig { // TODO: 增加玩家属性，例如玩家的道具，武器，技能等。在每一个关卡都需要改变
    // Listing features of the level
    private double monsterScalDensity;
    private double monsterScalSpeed;
    private double monsterScalHP;
    private double monsterScalDamage;
    private double monsterScalCoin;

    private int duration;

    private BasePlayer player; // 需要修改为从配置文件中读取玩家属性，例如玩家的道具，武器，技能等。在每一个关卡都需要改变
 
    private final List<MonsterEgg> monsterEggList;

    public LevelConfig() {
        this.monsterEggList = new ArrayList<>(); // INFO: 临时用，需要修改为从配置文件中读取怪物属性，例如怪物的血量，攻击力，速度等。在每一个关卡都需要改变
    }

    @Getter
    public static class MonsterEgg {
        private MonsterTypes monsterType;
        @Setter
        private double spawnTime;
        @Setter
        private int spawnCount;

        public MonsterEgg(MonsterTypes monsterType, double spawnTime, int spawnCount) {
            this.monsterType = monsterType;
            this.spawnTime = spawnTime;
            this.spawnCount = spawnCount;
        }
    }
}
