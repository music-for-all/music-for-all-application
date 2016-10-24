package com.musicforall.services.notification;

import com.musicforall.notifications.Notification;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author IliaNik on 21.10.2016.
 */
public interface NotificationService {

    void fire(Integer userId, Notification.Type type);

    void resetUnreadNum();

    DeferredResult subscribe(Notification.Type type, Object timeoutResult);

    Integer getUnreadNum(Integer userId);
}
