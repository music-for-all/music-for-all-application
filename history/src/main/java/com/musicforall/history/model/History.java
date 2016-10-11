package com.musicforall.history.model;


import com.musicforall.common.Constants;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

/**
 * @author IliaNik on 17.07.2016.
 */
/*
@NamedQueries({
        @NamedQuery(
                name = History.USERS_HISTORIES_QUERY,
                query = " from History history" +
                        " where history.userId IN :usersIds" +
                        " and day(current_date()) - day(history.date) <= 1" +
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
        )
})
*/
@MappedSuperclass
public class History {

    public static final String POPULAR_TRACKS_QUERY = "most_popular_tracks";

    public static final String TRACK_LIKES_COUNT_QUERY = "get_likes_count";

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

    public History(Date date, Integer userId) {
        this.date = new Timestamp(date.getTime());
        this.userId = userId;
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


    @Override
    public int hashCode() {
        return Objects.hash(id, userId, date);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        final History other = (History) obj;
        return Objects.equals(this.id, other.id)
                && Objects.equals(this.userId, other.userId)
                && Objects.equals(this.date.getTime(), other.date.getTime());
    }

    @Override
    public String toString() {
        return "UsageHistory{" +
                "id=" + id +
                ", user_id=" + userId +
                ", date='" + date + '\'' +
                '}';
    }
}