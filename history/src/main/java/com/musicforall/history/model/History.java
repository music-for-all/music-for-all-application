package com.musicforall.history.model;


import com.musicforall.common.Constants;
import com.musicforall.history.handlers.events.EventType;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

/**
 * @author IliaNik on 17.07.2016.
 */

@Entity
@NamedQueries({
        @NamedQuery(
                name = History.USERS_HISTORIES_QUERY,
                query = " from History history" +
                        " where history.userId IN :usersIds" +
                        " and DATEDIFF(day, history.date, current_date()) <= 1" +
                        " order by history.date desc"),
        @NamedQuery(
                name = History.POPULAR_TRACKS_QUERY,
                query = "select history.trackId" +
                        " from History history" +
                        " where history.eventType=:eventType" +
                        " group by history.trackId" +
                        " order by count(history.trackId) desc"),
        @NamedQuery(
                name = History.TRACK_LIKES_COUNT_QUERY,
                query = "select count(*) from History history " +
                        "where history.trackId=:trackId and history.eventType=:eventType"),
        @NamedQuery(
                name = History.ALL_USERS_BY_TYPE_QUERY,
                query = "select h from History h where h.eventType = :eventType and h.userId in " +
                        "(:usersIds) order by h.date desc"
        ),
        @NamedQuery(
                name = History.GET_TRACKS_LIM,
                query = "select h.trackId " +
                        "from History h " +
                        "where h.userId = :userId " +
                        "and h.eventType = :eventType"
        )
})

@Table(name = "history")
public class History {

    public static final String POPULAR_TRACKS_QUERY = "most_popular_tracks";

    public static final String TRACK_LIKES_COUNT_QUERY = "get_likes_count";

    public static final String USERS_HISTORIES_QUERY = "get_users_histories";

    public static final String ALL_USERS_BY_TYPE_QUERY = "all_for_users_by_type";

    public static final String GET_TRACKS_LIM = "get_tracks_lim";

    @Id
    @Column(name = Constants.ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "track_id")
    private Integer trackId;

    @Column(name = "track_name")
    private String trackName;

    @Column(name = "playlist_id")
    private Integer playlistId;

    @Column(name = "playlist_name")
    private String playlistName;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "date", nullable = false)
    private Timestamp date;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;

    public History(Integer trackId, Integer playlistId, Date date, Integer userId, EventType eventType) {
        this.trackId = trackId;
        this.playlistId = playlistId;
        this.date = new Timestamp(date.getTime());
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
        return new Date(date.getTime());
    }

    public void setDate(Date date) {
        this.date = new Timestamp(date.getTime());
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

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

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, trackId, date, eventType, playlistId);
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
                && Objects.equals(this.playlistId, other.playlistId)
                && Objects.equals(this.userId, other.userId)
                && Objects.equals(this.date.getTime(), other.date.getTime())
                && Objects.equals(this.eventType, other.eventType);
    }

    @Override
    public String toString() {
        return "UsageHistory{" +
                "id=" + id +
                ", track_id=" + trackId +
                ", playlist_id=" + playlistId +
                ", user_id=" + userId +
                ", date='" + date + '\'' +
                ", eventType=" + eventType +
                '}';
    }
}