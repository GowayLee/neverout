package com.mambastu.controller.level.context.dto;

import com.mambastu.controller.level.context.dto.config.LevelConfig;
import com.mambastu.controller.level.context.dto.record.GlobalRecord;
import com.mambastu.controller.level.context.dto.record.LevelRecord;
import com.mambastu.controller.level.context.enums.GameMode;
import com.mambastu.material.pojo.entity.player.PlayerTypes;

import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.Setter;

public class Context {
    @Getter
    private final SimpleObjectProperty<GameMode> gameMode; // 游戏模式
    @Getter
    private final SimpleObjectProperty<PlayerTypes> playerType; // 玩家类型
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
        this.gameMode = new SimpleObjectProperty<>(GameMode.NORMAL); // 默认普通模式
        this.playerType = new SimpleObjectProperty<>(PlayerTypes.NormalPlayer); // 默认普通玩家
    }

}
