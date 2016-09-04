package com.musicforall.history.handlers.events;

public class TrackEvent extends Event {

    private final int trackId;

    public TrackEvent(int trackId, int userId, EventType type) {
        super(userId, type);
        this.trackId = trackId;
    }

    public int getTrackId() {
        return trackId;
    }
}
