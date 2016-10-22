package com.musicforall.common.notification;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author ENikolskiy.
 */
@Component
public class Notifier {
    public Integer checkingNum = 0;

    private final Queue<Notification> queue = new ConcurrentLinkedQueue<>();

    public <T> DeferredResult<T> deffer(Callable<T> callable) {
        final DeferredResult<T> result = new DeferredResult<>();
        final Notification<T> notification = new Notification<T>(result, callable);

        final Runnable remove = () -> queue.removeIf(n -> n.getId().equals(notification.getId()));
        result.onCompletion(remove);

        this.queue.add(notification);
        return result;
    }

    @Scheduled(fixedRate = 2000)
    public void doNotify() throws Exception {
        for (Notification notification : queue) {
            notification.doNotify();
        }
    }
}