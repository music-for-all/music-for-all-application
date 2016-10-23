package com.musicforall.web.feed;

import com.musicforall.services.notification.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author IliaNik on 31.08.2016.
 */
@Controller
public class FeedController {

    private static final Logger LOG = LoggerFactory.getLogger(FeedController.class);

    @Autowired
    private NotificationService notificationService;

    @RequestMapping("/feed")
    public String friendsActivity(Model model) {
        notificationService.resetUnreadNum();
        return "followingHistory";
    }
}

