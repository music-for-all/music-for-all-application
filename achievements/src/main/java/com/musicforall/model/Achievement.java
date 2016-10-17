package com.musicforall.model;

import com.google.common.base.MoreObjects;
import com.musicforall.common.Constants;
import com.musicforall.history.handlers.events.EventType;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author ENikolskiy.
 */
@Entity
@Table(name = "achievements")
public class Achievement {

    @Id
    @Column(name = Constants.ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "script", nullable = false)
    private String script;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;

    @Column
    private int count;

    public Achievement() {
    }

    public Achievement(String script, EventType eventType, int count) {
        this.script = script;
        this.eventType = eventType;
        this.count = count;
    }

    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Achievement that = (Achievement) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(script, that.script) &&
                Objects.equals(count, that.count) &&
                Objects.equals(eventType, that.eventType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, script, eventType, count);
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("script", script)
                .add("eventType", eventType)
                .toString();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
