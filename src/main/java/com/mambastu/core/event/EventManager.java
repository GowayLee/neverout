package com.mambastu.core.event;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.mambastu.core.event.comp.event.BaseEvent;
import com.mambastu.core.event.comp.handler.BaseEventHandler;

public class EventManager {
    private static EventManager INSTANCE = new EventManager();

    private final Map<Class<? extends BaseEvent>, BaseEventHandler<? extends BaseEvent>> handlerMap = new HashMap<>();

    private EventManager() {
    }

    public static EventManager getInstance() {
        return INSTANCE;
    }

    public <T extends BaseEvent> void register(Class<T> eventType, BaseEventHandler<T> handler) {
        handlerMap.put(eventType, handler);
    }

    public <T extends BaseEvent> void unregister(Class<T> eventType, BaseEventHandler<T> handler) {
        if (handlerMap.containsKey(eventType)) {
            handlerMap.remove(eventType, handler);
        }
    }

    public <T extends BaseEvent> void fireEvent(T event) {
        BaseEventHandler<? extends BaseEvent> handler = handlerMap.get(event.getClass());
        if (handler == null) { // 动态加载Handler
            loadHandlerForEvent(event.getClass());
            handler = handlerMap.get(event.getClass());
        }

        if (handler != null) {
            try {
                @SuppressWarnings("unchecked")
                BaseEventHandler<T> eventHandler = (BaseEventHandler<T>) handler; // 强制向下转型，类型不安全
                eventHandler.handle(event);
            } catch (ClassCastException e) {
                e.printStackTrace();
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
