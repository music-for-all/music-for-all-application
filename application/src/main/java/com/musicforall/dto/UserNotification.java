package com.musicforall.dto;

import com.musicforall.common.notification.Notification;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;

/**
 * @author Evgeniy on 22.10.2016.
 */
public class UserNotification extends Notification {
    private final Integer userId;

    public UserNotification(DeferredResult deferredResult, Callable<Integer> callable, Integer userId) {
        super(deferredResult, callable);
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }
}
