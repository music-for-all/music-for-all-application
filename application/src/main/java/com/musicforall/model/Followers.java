package com.musicforall.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * Created by andrey on 8/2/16.
 */
@Entity
@Table(name = "followers")
public class Followers implements Serializable {

    private static final long serialVersionUID = 1936826830874885637L;

    @Id
    @Column(name = "follower_id", nullable = false)
    private Integer followerId;

    @ElementCollection
    @CollectionTable(name = "user_followers", joinColumns = @JoinColumn(name = "followers_id"))
    @Column(name = "following_id")
    private final Set<Integer> followingId = new HashSet<>();

    public Followers() {

    }

    public Followers(Integer followerId) {
        this.followerId = followerId;
    }

    public void follow(Integer followingId) {
        this.followingId.add(followingId);
    }

    public void unfollow(Integer followingId) {
        this.followingId.remove(followingId);
    }

    public Set<Integer> getFollowingId() {
        return followingId;
    }

    public Integer getFollowerId() {
        return followerId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(followerId, followingId);
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
        return Objects.equals(this.followerId, other.followerId)
                && Objects.equals(this.followingId, other.followingId);
    }

    @Override
    public String toString() {
        return "Followers{" +
                "follower_id=" + followerId +
                ", following_id=" + followingId + '}';
    }
}
