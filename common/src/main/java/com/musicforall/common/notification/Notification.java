package com.musicforall.common.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.UUID;
import java.util.concurrent.Callable;

import static java.util.Objects.requireNonNull;

/**
 * @author ENikolskiy.
 */
public class Notification {
    private static final Logger LOG = LoggerFactory.getLogger(Notification.class);

    private String id = UUID.randomUUID().toString();
    private DeferredResult deferredResult;
    private Callable callable;

    public Notification(DeferredResult deferredResult, Callable callable) {
        requireNonNull(deferredResult, "deferredResult must not be null");
        requireNonNull(callable, "callable must not be null");
        this.deferredResult = deferredResult;
        this.callable = callable;
    }

    public void doNotify() {
        Object result = null;
        try {
            result = callable.call();
        } catch (Exception e) {
            LOG.error("Deferred action failed", e);
        }
        if (result == null) {
            return;
        }
        deferredResult.setResult(result);
    }

    public String getId() {
        return id;
    }

    public DeferredResult getDeferredResult() {
        return deferredResult;
    }

    public Callable getCallable() {
        return callable;
    }
}
