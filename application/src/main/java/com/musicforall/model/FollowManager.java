package com.musicforall.model;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by andrey on 8/2/16.
 */
@Embeddable
public class FollowManager implements Serializable {

    @ManyToMany(fetch = FetchType.EAGER)
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinTable(name = "followers",
            joinColumns = {@JoinColumn(name = "following_id")},
            inverseJoinColumns = {@JoinColumn(name = "followers_id")})
    private Set<User> followers;

    @ManyToMany(fetch = FetchType.EAGER)
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinTable(name = "followers",
            joinColumns = {@JoinColumn(name = "followers_id")},
            inverseJoinColumns = {@JoinColumn(name = "following_id")})
    private Set<User> following;

    public FollowManager() {
        followers = new HashSet<>();
        following = new HashSet<>();
    }

    public void follow(User following_user) {
        following.add(following_user);
    }

    public void unfollow(User following_user) {
        following.remove(following_user);
    }

    public Set<User> getFollowing() {
        return following;
    }

    public Set<User> getFollowers() {
        return followers;
    }
}
