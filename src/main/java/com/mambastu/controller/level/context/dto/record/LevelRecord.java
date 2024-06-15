package com.mambastu.controller.level.context.dto.record;

import com.mambastu.material.pojo.entity.player.BasePlayer;

import javafx.beans.property.SimpleIntegerProperty;
import lombok.Getter;

@Getter
public class LevelRecord { // LevelRecord类用于存储关卡游戏实时信息
    private final SimpleIntegerProperty levelNum; // 关卡编号

    private final BasePlayer player; // 玩家对象中包含玩家的HP等信息

    private final SimpleIntegerProperty remainDuration;// 剩余时间
    private final SimpleIntegerProperty killCount; // 击杀数量
    private final SimpleIntegerProperty totalEnemies; // 总敌人数量
    private final SimpleIntegerProperty remainingEnemies; // 剩余敌人数量

    public LevelRecord(int levelNum, BasePlayer player) { // 构造函数，初始化关卡信息
        this.levelNum = new SimpleIntegerProperty(levelNum); // 设置关卡编号
        this.remainDuration = new SimpleIntegerProperty(0); // 初始化剩余时间为0
        this.killCount = new SimpleIntegerProperty(0); // 初始化击杀数量为0
        this.totalEnemies = new SimpleIntegerProperty(0); // 初始化总敌人数量为0
        this.remainingEnemies = new SimpleIntegerProperty(0); // 初始化剩余敌人数量为0
        this.player = player;
    }
}
