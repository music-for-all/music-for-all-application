package com.musicforall.cache;

import com.musicforall.common.cache.CacheProvider;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.concurrent.atomic.AtomicInteger;

import static com.musicforall.config.CacheConfig.NOTIFICATION;

/**
 * @author IliaNik on 20.10.2016.
 */

@CacheConfig(cacheNames = NOTIFICATION)
public class NotificationCacheProvider implements CacheProvider<Integer, AtomicInteger> {
    private static final String SPEL_KEY = "#key";

    @Override
    @CachePut(key = SPEL_KEY)
    public AtomicInteger put(Integer key, AtomicInteger value) {
        return value;
    }

    @Override
    @Cacheable(key = SPEL_KEY)
    public AtomicInteger get(Integer key) {
        return null;
    }

    @Override
    @Cacheable(key = SPEL_KEY)
    @CacheEvict(key = SPEL_KEY)
    public AtomicInteger remove(Integer key) {
        return null;
    }
}