package com.musicforall.notifications;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

/**
 * @author ENikolskiy.
 */
public abstract class Notification {
    private Type type;
    private String id = UUID.randomUUID().toString();

    public Notification(Type type) {
        requireNonNull(type, "Notification type must not be null");
        this.type = type;
    }

    public abstract void doNotify();

    public String getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        FOLLOWER
    }
}
