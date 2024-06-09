package com.mambastu.controller.level.context.dto.record;

import com.mambastu.material.pojo.entity.player.BasePlayer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LevelRecord { // LevelRecord类用于存储关卡游戏实时信息
    private int levelNum; // 关卡编号

    private final BasePlayer player; // 玩家对象中包含玩家的HP等信息

    private int killCount; // 击杀数量
    private int totalEnemies; // 总敌人数量
    private int remainingEnemies; // 剩余敌人数量
    private int coins; // 金币数量

    public LevelRecord(int levelNum, BasePlayer player) { // 构造函数，初始化关卡信息
        this.levelNum = levelNum;
        this.player = player;
    }
}
