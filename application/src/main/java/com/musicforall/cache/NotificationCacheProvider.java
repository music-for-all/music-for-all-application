package com.musicforall.cache;

import com.musicforall.common.cache.CacheProvider;
import com.musicforall.notifications.Notification;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.Collection;

import static com.musicforall.config.CacheConfig.NOTIFICATION;

/**
 * @author IliaNik on 20.10.2016.
 */

@CacheConfig(cacheNames = NOTIFICATION)
public class NotificationCacheProvider implements CacheProvider<Integer, Collection<Notification>> {
    private static final String SPEL_KEY = "#key";

    @Override
    @CachePut(key = SPEL_KEY)
    public Collection<Notification> put(Integer key, Collection<Notification> value) {
        return value;
    }

    @Override
    @Cacheable(key = SPEL_KEY)
    public Collection<Notification> get(Integer key) {
        return null;
    }

    @Override
    @Cacheable(key = SPEL_KEY)
    @CacheEvict(key = SPEL_KEY)
    public Collection<Notification> remove(Integer key) {
        return null;
    }
}