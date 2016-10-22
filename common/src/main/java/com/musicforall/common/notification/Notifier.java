package com.musicforall.common.notification;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;

/**
 * @author ENikolskiy.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Notifier<T extends Notification> {
    private final Queue<T> queue = new ConcurrentLinkedQueue<>();

    public DeferredResult add(T notification) {
        final DeferredResult result = notification.getDeferredResult();

        final Runnable remove = () -> queue.removeIf(n -> n.getId().equals(notification.getId()));
        result.onCompletion(remove);

        this.queue.add(notification);
        return result;
    }

    public void doNotify() {
        for (Notification notification : queue) {
            notification.doNotify();
        }
    }

    public void doNotifyWhen(Predicate<T> predicate) {
        queue.stream()
                .filter(predicate)
                .forEach(Notification::doNotify);
    }
}