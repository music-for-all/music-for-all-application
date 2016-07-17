package com.musicforall.events.table;

/**
 * @author IliaNik on 17.07.2016.
 */

import com.musicforall.events.handlers.EventType;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Objects;


@Entity
@Table(name = "usage_history")
public class UsageHistory {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "track_id")
    private int track_id;

    @Column(name = "user_id")
    private int user_id;

    @Pattern(regexp = "^[0-9]{2}/[0-9]{2}/[0-9]{4} ([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")
    @Column(name = "date")
    private String date;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    public UsageHistory(int track_id, int user_id, String date, EventType eventType) {
        this.track_id = track_id;
        this.user_id = user_id;
        this.date = date;
        this.eventType = eventType;
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
