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


    @Enumerated(EnumType.STRING)
    @Column(name = "event_type")
    private TrackEventType eventType;

    public TrackHistory(Integer trackId, Date date, Integer userId, TrackEventType eventType) {
        super(date, userId);
        this.eventType = eventType;
        this.trackId = trackId;
    }

    public TrackHistory() {

    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getId(), super.getUserId(), super.getDate(), trackId, eventType);
    }

    @Override
    public String toString() {
       return super.toString() +
                ", trackId='" + trackId+ '\'' +
                ", eventType=" + eventType +
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
                Objects.equals(this.trackId, other.trackId)
                && Objects.equals(this.eventType, other.eventType);
    }

    public void setTrackId(Integer trackId) {
        this.trackId = trackId;
    }

    public Integer getTrackId() {return this.trackId; }

    public void setEventType(TrackEventType eventType) {
        this.eventType = eventType;
    }

    public TrackEventType getEventType() {
        return this.eventType;
    }
}
