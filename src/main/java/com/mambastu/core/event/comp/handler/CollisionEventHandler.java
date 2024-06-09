package com.mambastu.core.event.comp.handler;

import com.mambastu.core.event.comp.event.CollisionEvent;

public class CollisionEventHandler extends BaseEventHandler<CollisionEvent> {
    @Override
    public void handle(CollisionEvent event) {
        event.getPlayer().getHP().set(event.getPlayer().getHP().get() - 1);
    }
}
