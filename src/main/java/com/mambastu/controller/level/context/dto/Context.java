package com.mambastu.controller.level.context.dto;

import com.mambastu.controller.level.context.dto.config.LevelConfig;
import com.mambastu.controller.level.context.dto.record.GlobalRecord;
import com.mambastu.controller.level.context.dto.record.LevelRecord;
import com.mambastu.controller.level.context.enums.GameMode;

import lombok.Getter;
import lombok.Setter;

public class Context {
    @Getter @Setter
    private GameMode gameMode;
    @Getter
    private final GlobalRecord globalRecord;

    // 每个关卡的参数与记录器, 在关卡开始前将实例化一个新的对象来引用
    @Getter
    private final LevelConfig levelConfig; // 每个关卡的参数
    @Getter @Setter
    private LevelRecord levelRecord; // 每个关卡的记录器, 在关卡开始前将实例化一个新的对象来引用
    

    public Context() {
        this.globalRecord = new GlobalRecord();
        this.levelConfig = new LevelConfig();
        this.gameMode = GameMode.NORMAL; // 默认NORMAL模式
    }

}
