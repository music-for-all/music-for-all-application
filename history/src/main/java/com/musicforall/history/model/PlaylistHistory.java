package com.musicforall.history.model;

import com.musicforall.history.handlers.events.PlaylistEventType;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Pukho on 11.10.2016.
 */
@Entity
@Table(name = "playlist_history")
public class PlaylistHistory extends History {

    @Column(name = "playlist_id")
    private Integer playlistId;


    @Column(name = "playlist_name")
    private String playlistName;

    public PlaylistHistory(Date date, Integer userId,
                           PlaylistEventType type, Integer playlistId, String playlistName) {
        super(date, userId, type);
        this.playlistId = playlistId;
        this.playlistName = playlistName;
    }
    public PlaylistHistory() {}
    public Integer getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(Integer playlistId) {
        this.playlistId = playlistId;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
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
                Objects.equals(this.playlistId, other.playlistId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getId(), super.getUserId(), super.getDate(), super.getEventType(), playlistId, playlistName);
    }

    @Override
    public String toString() {
        return super.toString() +
                ", playlistId='" + playlistId + '\'' +
                ", playlistName=" + playlistName +
                '}';
    }
}
