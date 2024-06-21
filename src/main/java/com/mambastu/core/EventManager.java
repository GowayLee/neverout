package com.mambastu.core;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mambastu.annotation.EventInfo;
import com.mambastu.core.event.BaseEvent;
import com.mambastu.core.event.handler.EventHandler;
import com.mambastu.enums.EventType;

public class EventManager {
    private static final Logger logger = LogManager.getLogger(EventManager.class); // 初始化日志记录器，用于记录程序运行过程中的日志信息

    private static EventManager INSTANCE = new EventManager();

    private final Map<EventType, EventHandler<? extends BaseEvent>> handlerMap = new HashMap<>();

    private EventManager() {
    }

    public static EventManager getInstance() {
        return INSTANCE;
    }

    public <T extends BaseEvent> void register(EventType eventType, EventHandler<T> handler) {
        handlerMap.put(eventType, handler);
        logger.info("Register event: " + eventType);
    }

    public <T extends BaseEvent> void unregister(EventType eventType, EventHandler<T> handler) {
        if (handlerMap.containsKey(eventType)) {
            handlerMap.remove(eventType, handler);
        }
    }

    public <T extends BaseEvent> void fireEvent(T event) {
        EventInfo eventInfo = event.getClass().getAnnotation(EventInfo.class);
        if (eventInfo == null) throw new IllegalArgumentException("Event class must be annotated with @EventInfo");
        EventType eventType = eventInfo.type();
        if (eventType == EventType.Other) throw new IllegalArgumentException("EventInfo annotation must have a non-null value for type()");
        EventHandler<? extends BaseEvent> handler = handlerMap.get(eventType);
        if (handler == null) { // 动态加载Handler
            loadHandlerForEvent(event.getClass(), eventType);
            handler = handlerMap.get(eventType);
        }
        if (handler != null) {
            try {
                @SuppressWarnings("unchecked")
                EventHandler<T> eventHandler = (EventHandler<T>) handler; // 强制向下转型，类型不安全
                eventHandler.handle(event);
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    private <T extends BaseEvent> void loadHandlerForEvent(Class<T> event, EventType eventType) {
        String handlerClassName = "com.mambastu.core.event.handler.impl." + event.getSimpleName() + "Handler";
        try {
            Class<?> handlerClass = Class.forName(handlerClassName);
            @SuppressWarnings("unchecked")
            EventHandler<T> handlerInstance = (EventHandler<T>) handlerClass.getDeclaredConstructor().newInstance(); // 强制向下转型，类型不安全
            register(eventType, handlerInstance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
