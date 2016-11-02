package com.musicforall.services.notification;

import com.musicforall.common.cache.CacheProvider;
import com.musicforall.notifications.DeferredConnection;
import com.musicforall.notifications.Notification;
import com.musicforall.notifications.Notifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.musicforall.config.CacheConfig.NOTIFICATION;

/**
 * @author IliaNik on 21.10.2016.
 */
@Service
public class NotificationServiceImpl implements NotificationService {
    private static final long TIMEOUT = 60000;

    @Autowired
    @Qualifier(NOTIFICATION)
    private CacheProvider<Integer, Collection<Notification>> cache;
    @Autowired
    private Notifier notifier;

    @Override
    public void fire(Integer userId, Notification.Type type) {
        Collection<Notification> notifications = cache.get(userId);

        if (notifications == null) {
            notifications = new ConcurrentLinkedQueue<>();
        }

        final boolean typeIsPresent = notifications.stream()
                .filter(n -> n.getType() == type)
                .count() > 0;

        if (!typeIsPresent) {
            notifications.add(new Notification(type));
            cache.put(userId, notifications);
        }
        incrementCountByType(notifications, type);
        notifier.fire(userId);
    }

    @Override
    public void clearNotifications(Integer userId) {
        cache.remove(userId);
    }

    @Override
    public DeferredResult<Collection<Notification>> subscribe(Integer userId, Object timeoutResult) {
        final DeferredResult<Collection<Notification>> result = new DeferredResult<>(TIMEOUT, timeoutResult);
        result.onCompletion(() -> notifier.unsubscribe(userId));

        final DeferredConnection<Collection<Notification>> connection = new DeferredConnection<>(result,
                () -> getUnread(userId));
        notifier.subscribe(userId, connection);
        return result;
    }

    @Override
    public Collection<Notification> getUnread(Integer userId) {
        return cache.get(userId);
    }

    private void incrementCountByType(Collection<Notification> notifications, Notification.Type type) {
        notifications.stream()
                .filter(n -> n.getType() == type)
                .forEach(Notification::increment);
    }
}
