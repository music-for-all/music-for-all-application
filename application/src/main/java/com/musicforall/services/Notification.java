package com.musicforall.services;

import org.springframework.web.context.request.async.DeferredResult;

import java.util.UUID;
import java.util.concurrent.Callable;

import static java.util.Objects.requireNonNull;

/**
 * @author ENikolskiy.
 */
//TODO move to common
public class Notification<T> {
    private String id = UUID.randomUUID().toString();
    private DeferredResult<T> result;
    private Callable<T> callable;

    public Notification(DeferredResult<T> result, Callable<T> callable) {
        requireNonNull(result, "result must not be null");
        requireNonNull(callable, "callable must not be null");
        this.result = result;
        this.callable = callable;
    }

    public void doNotify() throws Exception {
        result.setResult(callable.call());
    }

    public String getId() {
        return id;
    }
}
