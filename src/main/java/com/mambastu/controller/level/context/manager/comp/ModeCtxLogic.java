package com.mambastu.controller.level.context.manager.comp;

public interface ModeCtxLogic {

    void initLevelConfig();

    void updateLevelConfig();

    /**
     * 在关卡结束玩家完成购买, 开始下一关之前, 使Player中的所有道具生效,
     * 修改玩家各项数值, 达到提升属性的效果
     */
    void updatePlayerProp();

    void updateGlobalRecord();

    void recordBeforeOver();

    void refreshLevelRecord();
}
