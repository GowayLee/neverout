package com.mambastu.core.logic.comp;

import java.beans.PropertyChangeListener;


public interface ModeLogic {

    void addPropertyListener(PropertyChangeListener listener);

    /*
     * 更新所有游戏实体属性
     */
    void updateEntity(long elapsedTime);

    void initMonsterGenTimer();

    void initPlayer();
}
