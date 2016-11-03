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
                        " where  history.eventType=:eventType" +
                        " group by history.trackId" +
                        " order by count(history.trackId) desc"),
        @NamedQuery(
                name = History.TRACK_LIKES_COUNT_QUERY,
                query = "select count(*) from History history " +
                        "where history.trackId=:trackId and history.eventType=:eventType"),
        @NamedQuery(
                name = History.ALL_USERS_BY_TYPE_QUERY,
                query = "select h from History h where h.eventType = :eventType and h.userId in " +
                        "(:usersIds) order by h.date desc"),
        @NamedQuery(
                name = History.ALL_HISTORY_BY_TIME,
                query = "select h from History h " +
                        "where h.eventType=:eventType " +
                        "and (h.date between :begin and :end)"
                  //      "and h.date > :begin and h.date < :end2"
        )
})

@Table(name = "history")
public class History {

    public static final String POPULAR_TRACKS_QUERY = "most_popular_tracks";

    public static final String TRACK_LIKES_COUNT_QUERY = "get_likes_count";

    public static final String USERS_HISTORIES_QUERY = "get_users_histories";

    public static final String ALL_USERS_BY_TYPE_QUERY = "all_for_users_by_type";

    public static final String ALL_HISTORY_BY_TIME = "all_histories_by_time";

    public static Builder create() {
        return new Builder();
    }

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

    @Column(name = "artist_name")
    private String artistName;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "date", nullable = false)
    private Timestamp date;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;

    public History(Integer trackId, Integer playlistId, Date date, Integer userId,
                   EventType eventType) {
        this.trackId = trackId;
        this.playlistId = playlistId;
        this.date = new Timestamp(date.getTime());
        this.userId = userId;
        this.eventType = eventType;
    }

    public History(Integer trackId, Integer playlistId, String trackName, String playlistName,
                   Date date, Integer userId, EventType eventType, String artistName) {
        this.trackId = trackId;
        this.playlistId = playlistId;
        this.trackName = trackName;
        this.playlistName = playlistName;
        this.date = new Timestamp(date.getTime());
        this.userId = userId;
        this.eventType = eventType;
        this.artistName = artistName;
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

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
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
                ", artist_name" + artistName +
                ", playlist_id=" + playlistId +
                ", user_id=" + userId +
                ", date='" + date + '\'' +
                ", eventType=" + eventType +
                '}';
    }

    public static class Builder {

        private Integer trackId;

        private String trackName;

        private Integer playlistId;

        private String playlistName;

        private String artistName;

        private Integer userId;

        private Date date;

        private EventType eventType;

        public Builder() {
        }

        public Builder date(Date date) {
            this.date = date;
            return this;
        }

        public Builder playlistName(String playlistName) {
            this.playlistName = playlistName;
            return this;
        }
        public Builder trackName(String trackName) {
            this.trackName = trackName;
            return this;
        }
        public Builder eventType(EventType eventType) {
            this.eventType = eventType;
            return this;
        }
        public Builder trackId(Integer trackId) {
            this.trackId = trackId;
            return this;
        }
        public Builder artistName(String artistName) {
            this.artistName = artistName;
            return this;
        }

        public Builder userId(Integer userId) {
            this.userId = userId;
            return this;
        }

        public Builder playlistId(Integer playlistId) {
            this.playlistId = playlistId;
            return this;
        }

        public History get() {
            return new History(trackId, playlistId, trackName, playlistName,
                   date, userId, eventType, artistName);
        }
    }
}