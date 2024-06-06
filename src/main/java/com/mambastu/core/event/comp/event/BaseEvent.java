package com.mambastu.core.event.comp.event;

public abstract class BaseEvent {
    protected final String eventType;

    public BaseEvent(String eventType) {
        this.eventType = eventType;
    }

    public String getEventType() {
        return eventType;
    }
}
