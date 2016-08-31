package com.musicforall.web.following;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author IliaNik on 31.08.2016.
 */
@Controller
public class FollowingController {

    private static final Logger LOG = LoggerFactory.getLogger(FollowingController.class);

    public FollowingController() {
        LOG.info("");
    }

    @RequestMapping("/feed")
    public String friendsActivity(Model model) {
        return "followingHistory";
    }

}