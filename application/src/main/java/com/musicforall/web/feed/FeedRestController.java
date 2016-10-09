package com.musicforall.web.feed;

import com.musicforall.dto.userFeedsDTO.UserFeedsDTO;
import com.musicforall.services.feed.FeedService;
import com.musicforall.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author IliaNik on 30.08.2016.
 */
@RestController
@RequestMapping("/feed")
public class FeedRestController {
    private static final Logger LOG = LoggerFactory.getLogger(FeedRestController.class);

    @Autowired
    private FeedService feedService;

    @RequestMapping(value = "/histories", method = RequestMethod.GET)
    public List<UserFeedsDTO> getGroupedHistories() {
        return feedService
                .getGroupedFollowingFeeds(SecurityUtil.currentUserId());
    }
}
