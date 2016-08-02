package com.musicforall.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by andrey on 8/2/16.
 */
@Entity
@Table(name = "followers")
public class Followers implements Serializable {

    private static final long serialVersionUID = 1936826830874885637L;

    @Id
    @Column(name = "follower_id", nullable = false)
    private Integer follower_id;

    @Id
    @Column(name = "following_id", nullable = false)
    private Integer following_id;

    public Followers() {
    }

    public Followers(Integer follower_id, Integer following_id) {
        this.follower_id = follower_id;
        this.following_id = following_id;
    }

    public Integer getFollowingId() {
        return following_id;
    }

    public void setFollowingId(Integer following_id) {
        this.following_id = following_id;
    }

    public Integer getFollowerId() {
        return follower_id;
    }

    public void setFollowerId(Integer follower_id) {
        this.follower_id = follower_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(follower_id, following_id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Followers other = (Followers) obj;
        return Objects.equals(this.follower_id, other.follower_id)
                && Objects.equals(this.following_id, other.following_id);
    }

    @Override
    public String toString() {
        return "Followers{" +
                "follower_id=" + follower_id +
                ", following_id=" + following_id + '}';
    }
}
