package com.musicforall.notifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;

import static java.util.Objects.requireNonNull;

/**
 * @author ENikolskiy.
 */
public class DeferredNotification<R> extends Notification {
    private static final Logger LOG = LoggerFactory.getLogger(DeferredNotification.class);

    private DeferredResult<R> deferredResult;
    private Callable<R> callable;

    public DeferredNotification(DeferredResult<R> deferredResult, Callable<R> callable, Type type) {
        super(type);
        requireNonNull(deferredResult, "deferredResult must not be null");
        requireNonNull(callable, "callable must not be null");
        this.deferredResult = deferredResult;
        this.callable = callable;
    }

    @Override
    public void doNotify() {
        R result = null;
        try {
            result = callable.call();
        } catch (Exception e) {
            LOG.error("Deferred action failed", e);
        }
        deferredResult.setResult(result);
    }
}
