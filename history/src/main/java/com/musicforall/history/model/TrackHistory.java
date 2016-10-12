package com.musicforall.history.model;

import com.musicforall.history.handlers.events.TrackEventType;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Pukho on 11.10.2016.
 */
@Entity
@Table(name="track_entity")
public class TrackHistory extends History {

    @Column(name = "track_id")
    private Integer trackId;

    @Column(name = "track_name")
    private String trackName;

    public TrackHistory(Integer trackId, Date date, Integer userId, TrackEventType type, String trackName) {
        super(date, userId, type);
        this.trackName = trackName;
        this.trackId = trackId;
    }

    public TrackHistory() {}

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public void setTrackId(Integer trackId) {
        this.trackId = trackId;
    }

    public Integer getTrackId() {return this.trackId; }

    @Override
    public int hashCode() {
        return Objects.hash(super.getId(), super.getUserId(), super.getDate(), trackId, super.getEventType(), trackName);
    }

    @Override
    public String toString() {
       return super.toString() +
                ", trackId='" + trackId+ '\'' +
               ", trackName=" + trackName +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TrackHistory other = (TrackHistory) obj;
        return super.equals(obj) &&
                Objects.equals(this.trackId, other.trackId);

    }
}
