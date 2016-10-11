package com.musicforall.model;

import com.google.common.base.MoreObjects;
import com.musicforall.common.Constants;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author ENikolskiy.
 */
@Entity
@Table(name = "in_progress_achievements")
public class InProgressAchievement {
    @Id
    @Column(name = Constants.ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "achievement_id", nullable = false)
    @Cascade(CascadeType.SAVE_UPDATE)
    private Achievement achievement;

    @Column
    private int count;

    public InProgressAchievement() {
    }

    public InProgressAchievement(Achievement achievement) {
        this.achievement = achievement;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

    @Override
    public int hashCode() {
        return Objects.hash(id, achievement, count);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final InProgressAchievement other = (InProgressAchievement) obj;
        return Objects.equals(this.id, other.id)
                && Objects.equals(this.achievement, other.achievement)
                && Objects.equals(this.count, other.count);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("achievement", achievement)
                .add("count", count)
                .toString();
    }
}
