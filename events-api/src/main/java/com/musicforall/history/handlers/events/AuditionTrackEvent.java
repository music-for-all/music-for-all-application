package com.musicforall.history.handlers.events;

import com.musicforall.history.table.UsageHistory;

import java.util.Date;

/**
 * @author IliaNik on 17.07.2016.
 */
public class AuditionTrackEvent implements HistoryEvent {

    private final EventType eventType = EventType.TRACKAUDITIONED;

    private int track_id;
    private int user_id;
    private Date date = new Date();

    public EventType getEventType() {
        return eventType;
    }

    public int getTrack_id() {
        return track_id;
    }

    public void setTrack_id(int track_id) {
        this.track_id = track_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public UsageHistory getUsageHistory() {

        UsageHistory usageHistory = new UsageHistory(track_id, user_id, date, eventType);

        return usageHistory;
    }
}
