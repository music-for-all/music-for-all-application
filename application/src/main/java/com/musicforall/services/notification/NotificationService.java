package com.musicforall.services.notification;

import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author IliaNik on 21.10.2016.
 */
public interface NotificationService {

    void incrementNotifierNum(Integer userId);

    void resetNotifierNum();

    DeferredResult<Integer> getDeferredNotifierNum();
}
