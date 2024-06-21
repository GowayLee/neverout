package com.mambastu.controller.context.logic.impl;

import com.mambastu.controller.context.dto.Context;
import com.mambastu.controller.context.dto.config.LevelConfig;
import com.mambastu.controller.context.dto.config.LevelConfig.MonsterEgg;
import com.mambastu.controller.context.dto.record.GlobalRecord;
import com.mambastu.controller.context.dto.record.LevelRecord;
import com.mambastu.controller.context.logic.ModeCtxLogic;
import com.mambastu.enums.gameobjects.MonsterTypes;

public class NormalCtxImpl implements ModeCtxLogic{
    private final Context ctx;

    public NormalCtxImpl(Context ctx) {
        this.ctx = ctx; 
    }

    @Override
    public void initLevelConfig() { // 初始化第一个关卡配置信息，例如怪物密度、怪物伤害等。
        LevelConfig firstLevelConfig = ctx.getLevelConfig();
        firstLevelConfig.getMonsterEggList().clear();
        firstLevelConfig.getMonsterEggList().add(new MonsterEgg(MonsterTypes.HotMonster, 1.0, 999));
        firstLevelConfig.getMonsterEggList().add(new MonsterEgg(MonsterTypes.BossMonster, 2.4, 999));
        firstLevelConfig.setMonsterScalDensity(2000);
        firstLevelConfig.setMonsterScalCoin(1.0);
        firstLevelConfig.setDuration(20); // 基础关卡时长30秒。

    }

    @Override
    public void updateLevelConfig() { // 更新下一关的配置信息，例如怪物密度、怪物伤害等
        ctx.getLevelConfig().getMonsterEggList().stream()
            .filter(MonsterEgg -> MonsterEgg.getMonsterType() == MonsterTypes.HotMonster)
            .forEach(MonsterEgg -> {
                double oldTime = MonsterEgg.getSpawnTime();
                MonsterEgg.setSpawnTime(oldTime > 0.5 ? oldTime * 0.9 : oldTime);
            });
        ctx.getLevelConfig().getMonsterEggList().stream()
            .filter(MonsterEgg -> MonsterEgg.getMonsterType() == MonsterTypes.BossMonster)
            .forEach(MonsterEgg -> {
                double oldTime = MonsterEgg.getSpawnTime();
                MonsterEgg.setSpawnTime(oldTime > 1.0 ? oldTime * 0.9 : oldTime);
            });
        ctx.getLevelConfig().setDuration(ctx.getLevelConfig().getDuration() + 5); // 增加关卡时长10s
    }

    @Override
    public void updatePlayerProp() {
        ctx.getLevelRecord().getPlayer().getHP().set(ctx.getLevelRecord().getPlayer().getMaxHP().get()); // 玩家生命值恢复到上限。
        // 更新玩家属性，例如生命值、攻击力、防御力等。
    }

    @Override
    public void updateCoin() { // 更新玩家当前关卡获得的硬币数。
        int newCoin = (int) (ctx.getLevelRecord().getKillCount().get() * ctx.getLevelConfig().getMonsterScalCoin()); // 计算当前关卡击杀怪物获得的硬币数。
        ctx.getGlobalRecord().getCoins().set(ctx.getGlobalRecord().getCoins().get() + newCoin);
    }
    @Override

    public void updateGlobalRecord() { // 更新全局记录，例如玩家最高得分、最高关卡等。
        GlobalRecord globalRecord = ctx.getGlobalRecord();
        globalRecord.getTotalDuration().set(globalRecord.getTotalDuration().get() + ctx.getLevelConfig().getDuration()); // 更新总时长。
        globalRecord.getTotalKillCount().set(globalRecord.getTotalKillCount().get() + ctx.getLevelRecord().getKillCount().get());
        globalRecord.getTotalLevelNum().set(ctx.getLevelRecord().getLevelNum().get());
    }

    @Override
    public void recordBeforeOver() { // 记录关卡结束前的信息，例如玩家得分、剩余生命值等。
        GlobalRecord globalRecord = ctx.getGlobalRecord();
        globalRecord.getTotalDuration().set(globalRecord.getTotalDuration().get() + ctx.getLevelConfig().getDuration() - ctx.getLevelRecord().getRemainDuration().get()); // 减去当前关卡的剩余时长
        globalRecord.getTotalKillCount().set(globalRecord.getTotalKillCount().get() + ctx.getLevelRecord().getKillCount().get()); // 更新总击杀数。
        globalRecord.getTotalLevelNum().set(ctx.getLevelRecord().getLevelNum().get() - 1); // 更新总关卡数-1(在当前关卡失败)
    }

    @Override
    public void refreshLevelRecord() { // 刷新当前关卡的记录，例如玩家得分、剩余生命值等。
        ctx.setLevelRecord(new LevelRecord(ctx.getLevelRecord().getLevelNum().get() + 1, ctx.getLevelConfig().getPlayer())); // 关卡号+1, 传递玩家信息。
    }
}
