package com.musicforall.notifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;

import static java.util.Objects.requireNonNull;

/**
 * @author ENikolskiy.
 */
public class DeferredConnection<R> implements Notifier.Connection {
    private static final Logger LOG = LoggerFactory.getLogger(DeferredConnection.class);

    private DeferredResult<R> deferredResult;
    private Callable<R> callable;

    public DeferredConnection(DeferredResult<R> deferredResult, Callable<R> callable) {
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
