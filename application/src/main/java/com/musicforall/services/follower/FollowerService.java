package com.musicforall.services.follower;

import com.musicforall.model.User;

import java.util.List;

/**
 * Created by andrey on 8/2/16.
 */
public interface FollowerService {

    void follow(Integer userId, Integer following_userId);

    void unfollow(Integer userId, Integer following_userId);

    List<Integer> getFollowersId(Integer userId);

    List<Integer> getFollowingId(Integer userId);

    List<User> getFollowers(Integer userId);

    List<User> getFollowing(Integer userId);
}
