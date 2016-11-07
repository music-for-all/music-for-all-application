package com.musicforall.history.model;

import com.musicforall.history.handlers.events.EventType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Pukho on 03.11.2016.
 */

@Entity
@NamedQueries({
        @NamedQuery(
                name = TrackHistory.POPULAR_TRACKS_QUERY,
                query = "select history.trackId" +
                        " from TrackHistory history" +
                        " where  history.eventType=:eventType" +
                        " group by history.trackId" +
                        " order by count(history.trackId) desc"),
        @NamedQuery(
                name = TrackHistory.TRACK_LIKES_COUNT_QUERY,
                query = "select count(*) from TrackHistory history " +
                        "where history.trackId=:trackId and history.eventType=:eventType"),
        @NamedQuery(
                name = TrackHistory.ALL_HISTORY_BY_TIME,
                query = "select h from TrackHistory h " +
                        "where h.eventType=:eventType " +
                        "and (h.date between :begin and :end)"
                //      "and h.date > :begin and h.date < :end2"
        ),
})
public class TrackHistory extends History {



    public static final String ALL_HISTORY_BY_TIME = "all_histories_by_time";

    public static final String POPULAR_TRACKS_QUERY = "most_popular_tracks";

    public static final String TRACK_LIKES_COUNT_QUERY = "get_likes_count";

    @Column(name = "track_id")
    private Integer trackId;

    @Column(name = "track_name")
    private String trackName;

    @Column(name = "artist_name")
    private String artistName;

    public static TrackBuilder create() {return new TrackBuilder(); }

    public TrackHistory() {};


    public TrackHistory(Integer trackId, String trackName,
                        Date date, Integer userId, EventType eventType, String artistName,
                        Integer playlistId) {
        super(date, userId, eventType, playlistId);
        this.trackId = trackId;
        this.trackName = trackName;
        this.artistName = artistName;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", track_id=" + trackId +
                ", artist_name" + artistName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public Integer getTrackId() {
        return trackId;
    }

    public void setTrackId(Integer trackId) {
        this.trackId = trackId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getId(), super.getUserId(), super.getDate(),
                super.getEventType(), trackId, trackName, artistName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final TrackHistory other = (TrackHistory) obj;
        return super.equals(other)
                && Objects.equals(this.trackId, other.trackId)
                && Objects.equals(this.trackName, other.trackName)
                && Objects.equals(this.artistName, other.artistName);
    }

    public static class TrackBuilder {

        private Integer trackId;

        private String trackName;

        private String artistName;

        private Integer playlistId;

        public TrackBuilder() {

        }

        public TrackBuilder trackId(Integer trackId) {
            this.trackId = trackId;
            return this;
        }

        public TrackBuilder trackName(String trackName) {
            this.trackName = trackName;
            return this;
        }

        public TrackBuilder artistName(String artistName) {
            this.artistName = artistName;
            return this;
        }

        private Integer userId;

        private  Date date;

        private  EventType eventType;


        public TrackBuilder date(Date date) {
            this.date = date;
            return this;
        }

        public TrackBuilder eventType(EventType eventType) {
            this.eventType = eventType;
            return this;
        }

        public TrackBuilder userId(Integer userId) {
            this.userId = userId;
            return this;
        }

        public TrackBuilder playlistId(Integer playlistId) {
            this.playlistId = playlistId;
            return this;
        }

        public TrackHistory get() {
            return new TrackHistory(trackId, trackName,
                    date, userId, eventType, artistName, playlistId);
        }
    }
}