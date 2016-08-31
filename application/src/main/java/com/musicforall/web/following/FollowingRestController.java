package com.musicforall.web.following;

import com.musicforall.history.model.History;
import com.musicforall.model.User;
import com.musicforall.services.follower.FollowerService;
import com.musicforall.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author IliaNik on 30.08.2016.
 */
@RestController
@RequestMapping("/feed")
public class FollowingRestController {
    private static final Logger LOG = LoggerFactory.getLogger(FollowingRestController.class);

    @Autowired
    private FollowerService followerService;

    @RequestMapping(value = "/histories", method = RequestMethod.GET)
    public Map<User, List<History>> getGroupedHistories() {
        return followerService
                .getGroupedFollowingHistories(SecurityUtil.currentUser().getId());
    }
}
