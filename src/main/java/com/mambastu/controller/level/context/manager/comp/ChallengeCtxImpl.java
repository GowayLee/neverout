package com.mambastu.controller.level.context.manager.comp;

import com.mambastu.controller.level.context.dto.Context;
import com.mambastu.controller.level.context.dto.config.LevelConfig;
import com.mambastu.controller.level.context.dto.config.LevelConfig.MonsterEgg;
import com.mambastu.controller.level.context.dto.record.GlobalRecord;
import com.mambastu.controller.level.context.dto.record.LevelRecord;
import com.mambastu.material.pojo.entity.monster.MonsterTypes;

public class ChallengeCtxImpl implements ModeCtxLogic{
    private final Context ctx;

    public ChallengeCtxImpl(Context ctx) {
        this.ctx = ctx; 
    }

    @Override
    public void initLevelConfig() { // 初始化第一个关卡配置信息，例如怪物密度、怪物伤害等。
        LevelConfig firstLevelConfig = ctx.getLevelConfig();
        firstLevelConfig.getMonsterEggList().add(new MonsterEgg(MonsterTypes.HellLordMonster, 2.0, 1));
        firstLevelConfig.getMonsterEggList().add(new MonsterEgg(MonsterTypes.HotMonster, 3.5, 10));
        firstLevelConfig.setMonsterScalDensity(2000);
        firstLevelConfig.setMonsterScalCoin(1.0);
        firstLevelConfig.setDuration(9999); // 基础关卡时长30秒。

    }

    @Override
    public void updateLevelConfig() { // 更新下一关的配置信息，例如怪物密度、怪物伤害等
        return; // 挑战模式下不更新关卡配置。
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
