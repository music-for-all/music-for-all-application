package com.musicforall.history.handlers.events;

public class TrackEvent extends Event {

    private final Integer trackId;
    private final String trackName;

    public TrackEvent(Integer trackId, String trackName,
                      Integer userId, TrackEventType type) {
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
