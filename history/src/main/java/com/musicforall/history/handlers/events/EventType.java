package com.musicforall.history.handlers.events;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * @author IliaNik on 17.07.2016.
 */
public enum EventType {
    TRACK_LISTENED,
    TRACK_ADDED,
    TRACK_DELETED,
    PLAYLIST_ADDED,
    PLAYLIST_DELETED,
    TRACK_LIKED;

    public boolean in(final EventType... types) {
        requireNonNull(types, "Types must not be null");

        return Stream.of(types)
                .filter(t -> this == t)
                .findFirst()
                .isPresent();
    }
}