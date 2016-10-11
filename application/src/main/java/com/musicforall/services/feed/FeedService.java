package com.musicforall.services.feed;

import com.musicforall.dto.feed.Feed;
import com.musicforall.model.user.User;

import java.util.Collection;
import java.util.Map;

/**
 * @author IliaNik on 02.09.2016.
 */
public interface FeedService {
    Map<User, Collection<Feed>> getGroupedFollowingFeeds(Integer userId);
}
