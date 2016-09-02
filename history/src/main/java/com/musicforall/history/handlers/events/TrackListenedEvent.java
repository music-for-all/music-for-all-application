package com.musicforall.history.handlers.events;

/**
 * @author IliaNik on 17.07.2016.
 */
public class TrackListenedEvent {

    private Integer trackId;

    private Integer userId;


    public TrackListenedEvent(Integer trackId, Integer userId) {
        this.trackId = trackId;
        this.userId = userId;
    }

    public Integer getTrackId() {
        return trackId;
    }

    public void setTrackId(Integer trackId) {
        this.trackId = trackId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
