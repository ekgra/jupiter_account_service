package com.jupiter.accountservice.eventbus;

import java.util.*;

/**
 * A simple in-memory publish-subscribe event bus.
 */
public class EventBus {
    private final Map<Class<? extends BaseEvent>, List<EventHandler<?>>> handlers = new HashMap<>();

    public <T extends BaseEvent> void register(Class<T> eventType, EventHandler<T> handler) {
        handlers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(handler);
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseEvent> void publish(T event) {
        List<EventHandler<?>> eventHandlers = handlers.getOrDefault(event.getClass(), List.of());
        for (EventHandler<?> handler : eventHandlers) {
            ((EventHandler<T>) handler).handle(event);
        }
    }
}
