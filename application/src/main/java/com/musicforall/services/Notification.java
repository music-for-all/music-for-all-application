package com.musicforall.services;

import org.springframework.web.context.request.async.DeferredResult;

import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * @author ENikolskiy.
 */
//TODO move to common
public class Notification<T> {
    private String id = UUID.randomUUID().toString();
    private DeferredResult<T> result;
    private Callable<T> callable;

    public Notification(DeferredResult<T> result, Callable<T> callable) {
        this.result = result;
        this.callable = callable;
    }

    public DeferredResult<T> getResult() {
        return result;
    }

    public Callable<T> getCallable() {
        return callable;
    }

    public String getId() {
        return id;
    }
}
