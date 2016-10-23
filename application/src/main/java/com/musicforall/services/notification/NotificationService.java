package com.musicforall.services.notification;

import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author IliaNik on 21.10.2016.
 */
public interface NotificationService {

    void incrementUnreadNum(Integer userId);

    void resetUnreadNum();

    DeferredResult getDeferredUnreadNum(Object timeoutResult);

    Integer getUnreadNum(Integer userId);
}
