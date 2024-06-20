package com.mambastu.core.event.handler;

import com.mambastu.core.event.BaseEvent;

@FunctionalInterface
public interface EventHandler<T extends BaseEvent> {
    void handle(T event);
}
