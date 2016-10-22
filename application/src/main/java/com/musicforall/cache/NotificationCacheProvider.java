package com.musicforall.cache;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.musicforall.common.cache.CacheProvider;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.concurrent.atomic.AtomicInteger;

import static com.musicforall.cache.Action.*;
import static com.musicforall.config.CacheConfig.NOTIFICATION;

/**
 * @author IliaNik on 20.10.2016.
 */

@CacheConfig(cacheNames = NOTIFICATION)
public class NotificationCacheProvider implements CacheProvider<Integer, AtomicInteger> {
    private static final String SPEL_KEY = "#key";
    private final ListMultimap<Action, ActionListener> listeners = ArrayListMultimap.create();

    @Override
    @CachePut(key = SPEL_KEY)
    public AtomicInteger put(Integer key, AtomicInteger value) {
        fireAction(PUT);
        return value;
    }

    @Override
    @Cacheable(key = SPEL_KEY)
    public AtomicInteger get(Integer key) {
        fireAction(GET);
        return null;
    }

    @Override
    @Cacheable(key = SPEL_KEY)
    @CacheEvict(key = SPEL_KEY)
    public AtomicInteger remove(Integer key) {
        fireAction(REMOVE);
        return null;
    }

    private void fireAction(Action action) {
        listeners.get(action)
                .forEach(NotificationCacheProvider.ActionListener::onAction);
    }

    public void addListener(Action action, ActionListener listener) {
        listeners.put(action, listener);
    }

    public void removeListener(Action action, ActionListener listener) {
        listeners.remove(action, listener);
    }

    public void removeListener(Action action) {
        listeners.removeAll(action);
    }

    public interface ActionListener {
        void onAction();
    }
}