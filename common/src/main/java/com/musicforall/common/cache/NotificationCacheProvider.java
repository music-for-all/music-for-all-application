package com.musicforall.common.cache;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import static com.musicforall.common.cache.config.CacheConfig.NOTIFICATION;

/**
 * @author IliaNik on 20.10.2016.
 */

@CacheConfig(cacheNames = NOTIFICATION)
public class NotificationCacheProvider<K, V> implements CacheProvider<K, V> {

    private static final String SPEL_KEY = "#key";

    @Override
    @CachePut(value = NOTIFICATION, key = SPEL_KEY)
    public V put(K key, V value) {
        return value;
    }

    @Override
    @Cacheable(value = NOTIFICATION, key = SPEL_KEY)
    public V get(K key) {
        return null;
    }

    @Override
    @Cacheable(value = NOTIFICATION, key = SPEL_KEY)
    @CacheEvict(value = NOTIFICATION, key = SPEL_KEY)
    public V remove(K key) {
        return null;
    }
}