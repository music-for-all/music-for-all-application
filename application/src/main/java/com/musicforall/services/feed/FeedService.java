package com.musicforall.services.feed;

import com.musicforall.model.User;

import java.util.List;
import java.util.Map;

/**
 * @author IliaNik on 02.09.2016.
 */
public interface FeedService {
    Map<User, List<Feed>> getGroupedFollowingFeeds(Integer userId);
}
