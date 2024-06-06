package com.mambastu.core.event.comp.handler;

import com.mambastu.core.event.comp.event.BaseEvent;

public abstract class BaseEventHandler<T extends BaseEvent> {
    public abstract void handle(T event);
}
