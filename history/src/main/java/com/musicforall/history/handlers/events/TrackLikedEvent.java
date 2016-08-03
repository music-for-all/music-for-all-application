package com.musicforall.history.handlers.events;

import java.util.Date;

public class TrackLikedEvent {

    private int trackId;

    private int userId;

    private Date date = new Date();

    public TrackLikedEvent(int trackId, Date date, int userId) {
        this.trackId = trackId;
        this.date = date;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
