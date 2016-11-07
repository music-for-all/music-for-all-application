package com.musicforall.history.model;


import com.musicforall.common.Constants;
import com.musicforall.history.handlers.events.EventType;
import com.musicforall.history.handlers.events.PlaylistEvent;
import com.musicforall.history.service.history.SearchHistoryParams;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

/**
 * @author IliaNik on 17.07.2016.
 */

@MappedSuperclass
/*
@NamedQueries({
       /*@NamedQuery(
                name = History.USERS_HISTORIES_QUERY, //join ON
                query = " from History history" +
                        " where history.userId IN :usersIds" +
                        " and DATEDIFF(day, history.date, current_date()) <= 1" +
         @NamedQuery(
                name = History.ALL_USERS_BY_TYPE_QUERY,
                query = "select h from History h where h.eventType = :eventType and h.userId in " +
                        "(:usersIds) order by h.date desc"),
})*/


public class History {

    public static final String USERS_HISTORIES_QUERY = "get_users_histories";

    public static final String ALL_USERS_BY_TYPE_QUERY = "all_for_users_by_type";



    @Id
    @Column(name = Constants.ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "date", nullable = false)
    private Timestamp date;

    @Column(name = "playlist_id")
    private Integer playlistId;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;


    public History(Date date, Integer userId,
                   EventType eventType, Integer playlistId) {
        this.date = new Timestamp(date.getTime());
        this.userId = userId;
        this.eventType = eventType;
        this.playlistId = playlistId;
    }

    public History() {
    }

    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return new Date(date.getTime());
    }

    public void setDate(Date date) {
        this.date = new Timestamp(date.getTime());
    }

    public EventType getEventType() {
        return eventType;
    }

    public Integer getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(Integer playlistId) {
        this.playlistId = playlistId;
    }


    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, userId, date, eventType, playlistId);
    }

    @Override
    public boolean equals(Object obj) {
        final History other = (History) obj;
        return Objects.equals(this.id, other.id)
                && Objects.equals(this.playlistId, other.playlistId)
                && Objects.equals(this.userId, other.userId)
                && Objects.equals(this.date.getTime(), other.date.getTime())
                && Objects.equals(this.eventType, other.eventType);
    }

    @Override
    public String toString() {
        return "UsageHistory{" +
                "id=" + id +
                ", playlist_id=" + playlistId +
                ", user_id=" + userId +
                ", playlist_id" + playlistId +
                ", date='" + date + '\'' +
                ", eventType=" + eventType +
                '}';
    }
}