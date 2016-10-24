package com.musicforall.notifications;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author ENikolskiy.
 */
@Component
public class Notifier {
    private final Map<Integer, Collection<Notification>> map = new ConcurrentHashMap<>();

    public void subscribe(Integer userId, Notification notification) {
        Collection<Notification> notifications = map.get(userId);

        if (notifications == null) {
            notifications = new ConcurrentLinkedQueue<>();
        }

        notifications.add(notification);
        map.put(userId, notifications);
    }

    public void unsubscribe(Integer userId, Notification.Type type) {
        final Collection<Notification> notifications = map.get(userId);
        if (notifications != null) {
            notifications.removeIf(n -> type == n.getType());
        }
    }

    public void fire(Integer userId, Notification.Type type) {
        final Collection<Notification> notifications = map.get(userId);
        if (notifications != null) {
            notifications.stream()
                    .filter(n -> type == n.getType())
                    .forEach(Notification::doNotify);
        }
    }
}