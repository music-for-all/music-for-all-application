package com.musicforall.events.table;


import com.musicforall.events.handlers.EventType;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
/**
 * @author IliaNik on 17.07.2016.
 */

@Entity
@Table(name = "usage_history")
public class UsageHistory {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "track_id")
    private int track_id;

    @Column(name = "user_id", nullable = false)
    private int user_id;

    @Column(name = "date")
    private Date date;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    public UsageHistory(int track_id, int user_id, Date date, EventType eventType) {
        this.track_id = track_id;
        this.user_id = user_id;
        this.date = date;
        this.eventType = eventType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
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

    public int getTrack_id() {
        return track_id;
    }

    public void setTrack_id(int track_id) {
        this.track_id = track_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user_id, track_id, date, eventType);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final UsageHistory other = (UsageHistory) obj;
        return Objects.equals(this.id, other.id)
                && Objects.equals(this.track_id, other.track_id)
                && Objects.equals(this.user_id, other.user_id)
                && Objects.equals(this.date, other.date)
                && Objects.equals(this.eventType, other.eventType);
    }

    @Override
    public String toString() {
        return "UsageHistory{" +
                "id=" + id +
                ", track_id=" + track_id +
                ", user_id=" + user_id +
                ", date='" + date + '\'' +
                ", eventType=" + eventType +
                '}';
    }
}
