package com.musicforall.services.feed;


import com.musicforall.history.handlers.events.EventType;

import java.util.Date;
import java.util.Objects;

/**
 * @author IliaNik on 03.09.2016.
 */
public class Feed {

    private final EventType eventType;
    private final String target;
    private final Date date;
    private final String content;

    public Feed(EventType eventType, String target, Date date) {
        this.eventType = eventType;
        this.target = target;
        this.date = date;
        this.content = generateContent();
    }

    public Date getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public String generateContent() {
        switch (eventType) {
            case TRACK_LISTENED:
                return "Listened the track " + target;
            case TRACK_LIKED:
                return "Liked the track " + target;
            case TRACK_ADDED:
                return "Added the track " + target;
            case TRACK_DELETED:
                return "Deleted the track " + target;
            case PLAYLIST_ADDED:
                return "Added the playlist " + target;
            case PLAYLIST_DELETED:
                return "Deleted the playlist " + target;
            default:
                return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feed feed = (Feed) o;
        return Objects.equals(eventType, feed.eventType) &&
                Objects.equals(target, feed.target) &&
                Objects.equals(date, feed.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventType, target, date);
    }
}