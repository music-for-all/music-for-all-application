package com.musicforall.history.model;

import com.musicforall.history.handlers.events.EventType;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Pukho on 03.11.2016.
 */

@Entity
public class PlaylistHistory extends History {

    @Column(name = "playlist_name")
    private String playlistName;

    public static PlaylistBuilder create() {return new PlaylistBuilder(); }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public PlaylistHistory(Integer playlistId, String playlistName,
                   Date date, Integer userId, EventType eventType) {
        super(date, userId, eventType, playlistId);
        this.playlistName = playlistName;
    }

    public PlaylistHistory() {};

    @Override
    public String toString() {
        return super.toString() +
                ", playlist_name=" + playlistName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getId(), super.getUserId(), super.getDate(), super.getEventType(), playlistName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final PlaylistHistory other = (PlaylistHistory) obj;
        return super.equals(other)
                && Objects.equals(this.playlistName, other.playlistName);
    }

    public static class PlaylistBuilder {

        private Integer playlistId;

        private String playlistName;

        private Integer userId;

        private  Date date;

        private  EventType eventType;

        private PlaylistBuilder() {

        }

        public PlaylistBuilder date(Date date) {
            this.date = date;
            return this;
        }

        public PlaylistBuilder eventType(EventType eventType) {
            this.eventType = eventType;
            return this;
        }

        public PlaylistBuilder userId(Integer userId) {
            this.userId = userId;
            return this;
        }


        public PlaylistBuilder playlistName(String playlistName) {
            this.playlistName = playlistName;
            return this;
        }

        public PlaylistBuilder playlistId(Integer playlistId) {
            this.playlistId = playlistId;
            return this;
        }

        public PlaylistHistory get() {
            return new PlaylistHistory(playlistId, playlistName, date, userId, eventType);
        }
    }

}
