package com.musicforall.services.notification;

import com.musicforall.notifications.Notification;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Collection;

/**
 * @author IliaNik on 21.10.2016.
 */
public interface NotificationService {

    void fire(Integer userId, Notification.Type type);

    void clearNotifications(Integer userId);

    DeferredResult<Collection<Notification>> subscribe(Integer userId, Object timeoutResult);

    Collection<Notification> getUnread(Integer userId);
}
