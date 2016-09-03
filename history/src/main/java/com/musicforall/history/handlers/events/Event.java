package com.musicforall.history.handlers.events;

/**
 * @author ENikolskiy.
 */
public class Event {
    private final int userId;
    private final EventType type;

    protected Event(int userId, EventType type) {
        this.userId = userId;
        this.type = type;
    }

    public EventType getType() {
        return type;
    }

    public int getUserId() {
        return userId;
    }
}
