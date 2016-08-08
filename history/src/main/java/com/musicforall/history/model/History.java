package com.musicforall.history.model;


import com.musicforall.history.handlers.events.EventType;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * @author IliaNik on 17.07.2016.
 */

@Entity
@Table(name = "history")
public class History {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "track_id")
    private Integer trackId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "date", nullable = false)
    private Date date;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type")
    private EventType eventType;

    public History(Integer trackId, Date date, Integer userId, EventType eventType) {
        this.trackId = trackId;
        this.date = date;
        this.userId = userId;
        this.eventType = eventType;
    }

    public History() {
    }

    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
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

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, trackId, date, eventType);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final History other = (History) obj;
        return Objects.equals(this.id, other.id)
                && Objects.equals(this.trackId, other.trackId)
                && Objects.equals(this.userId, other.userId)
                && Objects.equals(this.date, other.date)
                && Objects.equals(this.eventType, other.eventType);
    }

    @Override
    public String toString() {
        return "UsageHistory{" +
                "id=" + id +
                ", track_id=" + trackId +
                ", user_id=" + userId +
                ", date='" + date + '\'' +
                ", eventType=" + eventType +
                '}';
    }
}
