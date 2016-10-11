package com.musicforall.history.model;

import com.musicforall.history.handlers.events.PlaylistEventType;

import javax.persistence.*;
import java.util.Objects;

/**
 * Created by Pukho on 11.10.2016.
 */
@Entity
@Table(name = "playlist_history")
public class PlaylistHistory extends History {

    @Column(name = "playlist_id")
    private Integer playlistId;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type")
    private PlaylistEventType eventType;

    public PlaylistEventType getEventType() {
        return eventType;
    }

    public void setEventType(PlaylistEventType eventType) {
        this.eventType = eventType;
    }

    public Integer getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(Integer playlistId) {
        this.playlistId = playlistId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PlaylistHistory other = (PlaylistHistory) obj;
        return super.equals(other) &&
                Objects.equals(this.playlistId, other.playlistId)
                && Objects.equals(this.eventType, other.eventType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getId(), super.getUserId(), super.getDate(), playlistId, eventType);
    }

    @Override
    public String toString() {
        return super.toString() +
                ", playlistId='" + playlistId + '\'' +
                ", eventType=" + eventType +
                '}';
    }
}
