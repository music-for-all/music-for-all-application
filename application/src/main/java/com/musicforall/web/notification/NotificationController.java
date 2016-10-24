package com.musicforall.web.notification;

import com.musicforall.notifications.Notification;
import com.musicforall.services.notification.NotificationService;
import com.musicforall.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import static org.springframework.http.HttpStatus.REQUEST_TIMEOUT;

/**
 * @author IliaNik on 23.10.2016.
 */
@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private static final Logger LOG = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "/subscribe", method = RequestMethod.POST)
    public DeferredResult subscribe(@RequestParam Notification.Type type) {
        return notificationService.subscribe(type, new ResponseEntity<>(REQUEST_TIMEOUT));
    }

    @RequestMapping(value = "/unreadCount", method = RequestMethod.GET)
    public Integer getUnreadNum() {
        final Integer userId = SecurityUtil.currentUserId();
        return notificationService.getUnreadNum(userId);
    }
}
