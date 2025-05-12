package com.jupiter.accountservice.eventbus;

/**
 * A generic handler for a specific type of event.
 * @param <T> the event type
 */
@FunctionalInterface
public interface EventHandler<T extends BaseEvent> {
    void handle(T event);
}
