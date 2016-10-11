package com.musicforall.model.user;

import com.google.common.base.MoreObjects;
import com.musicforall.common.Constants;
import com.musicforall.model.Achievement;
import com.musicforall.model.InProgressAchievement;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

/**
 * @author ENikolskiy.
 */
@Entity
@Table(name = "user_achievements")
public class UserAchievements {
    @Id
    @Column(name = Constants.ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Integer id;

    @OneToMany
    @JoinColumn(name = "user_achievements_id")
    private Set<InProgressAchievement> inProgress;

    @ManyToMany
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinTable(name = "done_achievements",
            joinColumns = {@JoinColumn(name = "user_achievements_id")},
            inverseJoinColumns = {@JoinColumn(name = "achievement_id")})
    private Set<Achievement> done;

    public UserAchievements() {
    }

    public UserAchievements(Set<InProgressAchievement> inProgress, Set<Achievement> done) {
        this.inProgress = inProgress;
        this.done = done;
    }

    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    public Set<InProgressAchievement> getInProgress() {
        return inProgress;
    }

    public void setInProgress(Set<InProgressAchievement> inProgress) {
        this.inProgress = inProgress;
    }

    public Set<Achievement> getDone() {
        return done;
    }

    public void setDone(Set<Achievement> done) {
        this.done = done;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, inProgress, done);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final UserAchievements other = (UserAchievements) obj;
        return Objects.equals(this.id, other.id)
                && Objects.equals(this.inProgress, other.inProgress)
                && Objects.equals(this.done, other.done);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("inProgress", inProgress)
                .add("done", done)
                .toString();
    }
}
