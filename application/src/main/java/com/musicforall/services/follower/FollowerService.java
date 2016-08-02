package com.musicforall.services.follower;

import com.musicforall.model.Followers;
import com.musicforall.model.User;

import java.util.Set;

/**
 * Created by andrey on 8/2/16.
 */
public interface FollowerService {

    void follow(Integer userId, Integer following_userId);

    void unfollow(Integer userId, Integer following_userId);

    Set<Followers> getFollowerId(Integer userId);

    Set<Followers> getFollowingId(Integer userId);

    Set<User> getFollower(Integer userId);

    Set<User> getFollowing(Integer userId);
}
