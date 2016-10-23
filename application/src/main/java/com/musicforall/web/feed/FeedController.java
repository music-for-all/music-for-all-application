package com.musicforall.web.feed;

import com.musicforall.services.notification.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import static org.springframework.http.HttpStatus.REQUEST_TIMEOUT;

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

    @ResponseBody
    @RequestMapping(value = "/unread_num", method = RequestMethod.GET)
    public DeferredResult getUnreadNum() {
        return notificationService.getDeferredUnreadNum(new ResponseEntity<>(REQUEST_TIMEOUT));
    }
}

