package com.mambastu.controller.context.dto.record;

import javafx.beans.property.SimpleIntegerProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GlobalRecord {
    private final SimpleIntegerProperty totalLevelNum; // 关卡编号
    private final SimpleIntegerProperty totalDuration; // 游戏时长
    private final SimpleIntegerProperty totalKillCount; // 击杀数量
    private final SimpleIntegerProperty coins; // 金币数量

    public GlobalRecord() {
        this.totalDuration = new SimpleIntegerProperty(0);
        this.totalKillCount = new SimpleIntegerProperty(0);
        this.totalLevelNum = new SimpleIntegerProperty(0);
        this.coins = new SimpleIntegerProperty(0);
    }
}
