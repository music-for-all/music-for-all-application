package com.musicforall.notifications;

import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.requireNonNull;

/**
 * @author ENikolskiy.
 */
public class Notification {
    private Type type;
    private AtomicInteger count = new AtomicInteger(0);

    public Notification(Type type) {
        requireNonNull(type, "Notification type must not be null");
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public AtomicInteger getCount() {
        return count;
    }

    public int increment() {
        return count.incrementAndGet();
    }

    public enum Type {
        FOLLOWER
    }
}
