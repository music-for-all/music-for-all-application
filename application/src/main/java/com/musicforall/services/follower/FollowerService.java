package com.musicforall.services.follower;

import com.musicforall.history.model.History;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by andrey on 8/2/16.
 */
public interface FollowerService {

    void follow(Integer userId, Integer following_userId);

    void unfollow(Integer userId, Integer following_userId);

    List<Integer> getFollowersId(Integer userId);

    Collection<Integer> getFollowingId(Integer userId);

    Map<Integer, List<History>> getGroupedFollowingHistories(Integer userId);
}
