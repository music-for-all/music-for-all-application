package com.musicforall.notifications;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ENikolskiy.
 */
@Component
public class Notifier {
    private final Map<Integer, Connection> connections = new ConcurrentHashMap<>();

    public void subscribe(Integer userId, Connection connection) {
        connections.put(userId, connection);
    }

    public void unsubscribe(Integer userId) {
        connections.remove(userId);
    }

    public void fire(Integer userId) {
        final Connection connection = connections.get(userId);
        if (connection != null) {
            connection.doNotify();
        }
    }

    public interface Connection {
        void doNotify();
    }
}