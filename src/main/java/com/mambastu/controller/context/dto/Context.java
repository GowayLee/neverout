package com.mambastu.controller.context.dto;

import com.mambastu.controller.context.dto.config.LevelConfig;
import com.mambastu.controller.context.dto.record.GlobalRecord;
import com.mambastu.controller.context.dto.record.LevelRecord;
import com.mambastu.enums.GameMode;
import com.mambastu.enums.gameobjects.PlayerTypes;
import com.mambastu.factories.PlayerFactory;
import com.mambastu.gameobjects.entity.player.BasePlayer;
import com.mambastu.gameobjects.weapon.PeaShooter;

import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.Setter;

public class Context {
    @Getter
    private final SimpleObjectProperty<GameMode> gameMode; // 游戏模式
    @Getter
    private final SimpleObjectProperty<PlayerTypes> playerType; // 玩家类型

    // 每个关卡的参数与记录器, 在关卡开始前将实例化一个新的对象来引用
    @Getter
    private final LevelConfig levelConfig; // 每个关卡的参数

    @Getter
    private GlobalRecord globalRecord;
    @Getter @Setter
    private LevelRecord levelRecord; // 每个关卡的记录器, 在关卡开始前将实例化一个新的对象来引用
    
    public Context() {
        this.globalRecord = new GlobalRecord();
        this.levelConfig = new LevelConfig();
        this.gameMode = new SimpleObjectProperty<>(GameMode.NORMAL); // 默认普通模式
        this.playerType = new SimpleObjectProperty<>(PlayerTypes.BasakerPlayer); // 默认普通玩家
    }

    public void initPlayer() {
        BasePlayer player =  PlayerFactory.getInstance().create(playerType.get());
        player.setWeapon(new PeaShooter());
        levelConfig.setPlayer(player); // 设置玩家到关卡配置中
    }

    public void initLevelRecord() {
        levelRecord = new LevelRecord(1, levelConfig.getPlayer());
    }

    public void initGlobalRecord() {
        globalRecord = new GlobalRecord();
    }

}
