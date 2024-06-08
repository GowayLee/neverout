package com.mambastu.controller.level.comp.config;

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
public class LevelConfig { // TODO: 增加玩家属性，例如玩家的道具，武器，技能等。在每一个关卡都需要改变
    // Listing features of the level
    private int levelNum;

    private double monsterScalNum = 2000;
    private double monsterScalSpeed;
    private double monsterScalHP;
    private double monsterScalDamage;
    private double monsterScalGold;

    private Class<? extends BasePlayer> playerEgg; // FIXME: 酱紫采用玩家蛋来生成玩家会导致难以在关卡间传递玩家的属性
    private Map<Class<? extends BaseMonster>, Double> monsterEggList;

    public LevelConfig() { // INFO: 测试用，写死
        monsterEggList = new HashMap<>();
        monsterEggList.put(HotMonster.class, 1.0);
        monsterEggList.put(BossMonster.class, 2.0);
        playerEgg = Player.class;
    }

}
