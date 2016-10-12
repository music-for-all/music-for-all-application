package com.musicforall.model.user;

import com.google.common.base.MoreObjects;
import com.musicforall.common.Constants;
import com.musicforall.model.Achievement;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author ENikolskiy.
 */
@NamedQueries(
        {
                @NamedQuery(
                        name = UserAchievement.INCREMENT_COUNT_QUERY,
                        query = "update UserAchievement ua " +
                                "set ua.progressCount = ua.progressCount + 1 " +
                                "where ua.id = :id")
        }
)
@Entity
@Table(name = "user_achievements")
public class UserAchievement {
    public static final String INCREMENT_COUNT_QUERY = "increment_counter";

    @Id
    @Column(name = Constants.ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Integer id;

    @OneToOne
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "achievement_id", nullable = false)
    private Achievement achievement;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column
    private int progressCount = 1;

    public UserAchievement() {
    }

    public UserAchievement(Achievement achievement, Status status) {
        this.achievement = achievement;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Achievement getAchievement() {
        return achievement;
    }

    public void setAchievement(Achievement achievement) {
        this.achievement = achievement;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getProgressCount() {
        return progressCount;
    }

    public void setProgressCount(int progressCount) {
        this.progressCount = progressCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAchievement that = (UserAchievement) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(achievement, that.achievement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, achievement);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("achievement", achievement)
                .add("status", status)
                .add("progressCount", progressCount)
                .toString();
    }

    public enum Status {
        IN_PROGRESS,
        DONE
    }
}
