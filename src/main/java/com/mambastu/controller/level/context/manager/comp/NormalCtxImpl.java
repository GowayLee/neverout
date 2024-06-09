package com.mambastu.controller.level.context.manager.comp;

import com.mambastu.controller.level.context.dto.Context;
import com.mambastu.controller.level.context.dto.config.LevelConfig;
import com.mambastu.controller.level.context.dto.record.LevelRecord;
import com.mambastu.material.pojo.entity.monster.BossMonster;
import com.mambastu.material.pojo.entity.monster.HotMonster;
import com.mambastu.material.pojo.entity.player.BasePlayer;

public class NormalCtxImpl implements ModeCtxLogic{
    private final Context ctx;

    public NormalCtxImpl(Context ctx) {
        this.ctx = ctx; 
    }

    @Override
    public void initLevelConfig(BasePlayer player) { // 初始化第一个关卡配置信息，例如怪物密度、怪物伤害等。
        LevelConfig firstLevelConfig = ctx.getLevelConfig();
        // TODO: 设置其他配置信息 ... // 例如，设置怪物密度、怪物伤害、关卡难度等。
        firstLevelConfig.setPlayer(player);
        firstLevelConfig.getMonsterEggList().put(HotMonster.class, 1.0);
        firstLevelConfig.getMonsterEggList().put(BossMonster.class, 2.0);
        firstLevelConfig.setMonsterScalDensity(2000);
    }

    @Override
    public void refreshLevelRecord() { // 刷新当前关卡的记录，例如玩家得分、剩余生命值等。
        if (ctx.getLevelRecord() == null) {
            System.out.println("666");
            ctx.setLevelRecord(new LevelRecord(1, ctx.getLevelConfig().getPlayer())); // 关卡号=1, 从LevelConfig中获取玩家信息。
        } else {
            ctx.setLevelRecord(new LevelRecord(ctx.getLevelRecord().getLevelNum() + 1, ctx.getLevelRecord().getPlayer())); // 关卡号+1, 传递玩家信息。
        }
    }

    @Override
    public void updateLevelConfig() { // 更新下一关的配置信息，例如怪物密度、怪物伤害等。
        // TODO: 实现逻辑...
        // 例如，增加怪物密度和伤害：ctx.setMonsterDensity(ctx.getMonsterDensity() + 0.1f); ctx.setMonsterDamage(ctx.getMonsterDamage() + 10f);  // 假设 ctx 是 Context 类型的实例。
        // 或者，增加关卡难度：ctx.setLevelDifficulty(ctx.getLevelDifficulty() + 1); // 假设 ctx 是 Context 类型的实例。
        // 等等...
    }
}
