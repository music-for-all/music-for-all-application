package com.musicforall.history.handlers.events;

/**
 * @author IliaNik on 17.07.2016.
 */
public class TrackListenedEvent {

    private int trackId;

    private int userId;


    public TrackListenedEvent(int trackId, int userId) {
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
