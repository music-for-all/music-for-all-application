package com.musicforall.history.handlers.events;

public class TrackLikedEvent {

    private int trackId;

    private int userId;

    public TrackLikedEvent(int trackId, int userId) {
        this.trackId = trackId;
        this.userId = userId;
    }

    public int getTrackId() {
        return trackId;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
