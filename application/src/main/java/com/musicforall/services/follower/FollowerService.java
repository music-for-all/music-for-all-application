package com.musicforall.services.follower;

import java.util.List;

/**
 * Created by andrey on 8/2/16.
 */
public interface FollowerService {

    /**
     * Registers a user with the given id as following another user.
     * @param userId the id of the follower
     * @param followingUserId the id of a user to follow
     */
    void follow(Integer userId, Integer followingUserId);

    /**
     * Unregisters a user with the given id as following another user.
     * @param userId the id of the follower
     * @param followingUserId the id of a user to unfollow
     */
    void unfollow(Integer userId, Integer followingUserId);

    /**
     * Gets the ids of the users following the user with the specified id.
     * @param userId the id of the followed user
     * @return the list of ids of followers
     */
    List<Integer> getFollowersId(Integer userId);

    /**
     * Gets the ids of the users followed by the user with the specified id.
     * @param userId the id of the user
     * @return the list of ids of followed users
     */
    List<Integer> getFollowingsIds(Integer userId);
}
