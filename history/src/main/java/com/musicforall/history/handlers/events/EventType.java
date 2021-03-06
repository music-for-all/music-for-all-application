package com.musicforall.history.handlers.events;

import static java.util.Objects.requireNonNull;

/**
 * @author IliaNik on 17.07.2016.
 */
public enum EventType {
    TRACK_LISTENED,
    TRACK_ADDED,
    TRACK_DELETED,
    TRACK_LIKED,
    PLAYLIST_ADDED,
    PLAYLIST_DELETED;

    public boolean in(final EventType... types) {
        requireNonNull(types, "Types must not be null");

        for (final EventType type : types) {
            if (this == type) {
                return true;
            }
        }

        return false;
    }

    public boolean isTrackEvent() {
        return in(
                EventType.TRACK_LISTENED,
                EventType.TRACK_ADDED,
                EventType.TRACK_DELETED,
                EventType.TRACK_LIKED);
    }

    public boolean isPlaylistEvent() {
        return in(
                EventType.PLAYLIST_ADDED,
                EventType.PLAYLIST_DELETED);
    }
}