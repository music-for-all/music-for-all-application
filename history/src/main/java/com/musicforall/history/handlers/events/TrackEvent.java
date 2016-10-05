package com.musicforall.history.handlers.events;

public class TrackEvent extends Event {

    private final int trackId;
    private final String trackName;

    public TrackEvent(int trackId, String trackName, int userId, EventType type) {
        super(userId, type);
        this.trackId = trackId;
        this.trackName = trackName;
    }

    public int getTrackId() {
        return trackId;
    }

    public String getTrackName() {
        return trackName;
    }
}
