package com.musicforall.history.handlers.events;

import com.musicforall.history.table.UsageHistory;

import java.util.Date;

/**
 * @author IliaNik on 17.07.2016.
 */
public class AuditionTrackEvent implements HistoryEvent {

    private final EventType eventType = EventType.TRACK_AUDITIONED;

    private int trackId;

    private int userId;

    private Date date = new Date();

    public AuditionTrackEvent(int trackId, Date date, int userId) {
        this.trackId = trackId;
        this.date = date;
        this.userId = userId;
    }

    public EventType getEventType() {
        return eventType;
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

    public UsageHistory getUsageHistory() {
        return new UsageHistory(trackId, date, userId, eventType);
    }
}
