package com.musicforall.common.notification;

import org.springframework.web.context.request.async.DeferredResult;

import java.util.UUID;
import java.util.concurrent.Callable;

import static java.util.Objects.requireNonNull;

/**
 * @author ENikolskiy.
 */
public class Notification<T> {
    private String id = UUID.randomUUID().toString();
    private DeferredResult<T> deferredResult;
    private Callable<T> callable;

    public Notification(DeferredResult<T> deferredResult, Callable<T> callable) {
        requireNonNull(deferredResult, "deferredResult must not be null");
        requireNonNull(callable, "callable must not be null");
        this.deferredResult = deferredResult;
        this.callable = callable;
    }

    public void doNotify() throws Exception {
        T result = callable.call();
        if (result == null) {
            return;
        }
        deferredResult.setResult(result);
    }

    public String getId() {
        return id;
    }
}
