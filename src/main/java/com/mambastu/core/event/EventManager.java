package com.mambastu.core.event;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mambastu.core.event.comp.event.BaseEvent;
import com.mambastu.core.event.comp.handler.BaseEventHandler;

public class EventManager {
    private final Map<Class<? extends BaseEvent>, Set<BaseEventHandler<? extends BaseEvent>>> listeners = new HashMap<>();

    public <T extends BaseEvent> void register(Class<T> eventType, BaseEventHandler<T> handler) {
        listeners.computeIfAbsent(eventType, k -> new HashSet<>()).add(handler);
    }

    public <T extends BaseEvent> void unregister(Class<T> eventType, BaseEventHandler<T> handler) {
        Set<BaseEventHandler<? extends BaseEvent>> handlers = listeners.get(eventType);
        if (handlers != null) {
            handlers.remove(handler);
            if (handlers.isEmpty()) {
                listeners.remove(eventType);
            }
        }
    }

    public <T extends BaseEvent> void eventTrigger(T event) {
        Set<BaseEventHandler<? extends BaseEvent>> handlers = listeners.get(event.getClass());
        if (handlers == null || handlers.isEmpty()) { // 动态加载Handler
            loadHandlerForEvent(event.getClass());
            handlers = listeners.get(event.getClass());
        }

        if (handlers != null) {
            for (BaseEventHandler<? extends BaseEvent> handler : handlers) {
                try {
                    @SuppressWarnings("unchecked")
                    BaseEventHandler<T> eventHandler = (BaseEventHandler<T>) handler; // 强制向下转型，类型不安全
                    eventHandler.handle(event);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private <T extends BaseEvent> void loadHandlerForEvent(Class<T> eventType) {
        String handlerClassName = "com.mambastu.core.event.comp.handler." + eventType.getSimpleName() + "Handler";
        try {
            Class<?> handlerClass = Class.forName(handlerClassName);
            @SuppressWarnings("unchecked")
            BaseEventHandler<T> handlerInstance = (BaseEventHandler<T>) handlerClass.getDeclaredConstructor().newInstance(); // 强制向下转型，类型不安全
            register(eventType, handlerInstance);
        } catch (ClassCastException | ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
