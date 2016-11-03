package com.musicforall.web.notification;

import com.musicforall.notifications.Notification;
import com.musicforall.services.notification.NotificationService;
import com.musicforall.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Collection;

import static org.springframework.http.HttpStatus.REQUEST_TIMEOUT;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author IliaNik on 23.10.2016.
 */
@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private static final Logger LOG = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(method = POST)
    public DeferredResult<Collection<Notification>> subscribe() {
        final Integer userId = SecurityUtil.currentUserId();
        return notificationService.subscribe(userId, new ResponseEntity<>(REQUEST_TIMEOUT));
    }

    @RequestMapping(method = GET)
    public Collection<Notification> getUnreadNum() {
        final Integer userId = SecurityUtil.currentUserId();
        return notificationService.getUnread(userId);
    }
}
