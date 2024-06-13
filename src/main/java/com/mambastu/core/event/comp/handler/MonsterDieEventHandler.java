package com.mambastu.core.event.comp.handler;

import com.mambastu.core.event.comp.event.MonsterDieEvent;
import com.mambastu.factories.MonsterFactory;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class MonsterDieEventHandler extends BaseEventHandler<MonsterDieEvent>{

    @Override
    public void handle(MonsterDieEvent event) {
        event.getMonster().die();
        Timeline rmTimer = new Timeline(new KeyFrame(Duration.seconds(1), e ->{
            event.getMonster().removeFromPane(event.getRoot()); // Remove from pane after 1 second.
            MonsterFactory.getInstance().delete(event.getMonster());
        }));
        rmTimer.play();
    }
}
