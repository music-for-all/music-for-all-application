package com.musicforall.services.feed;


import com.musicforall.dto.userFeedsDTO.UserFeedsDTO;

import java.util.List;

/**
 * @author IliaNik on 02.09.2016.
 */
public interface FeedService {
    List<UserFeedsDTO> getGroupedFollowingFeeds(Integer userId);
}
