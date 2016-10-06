package com.musicforall.common.cache;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import static com.musicforall.common.cache.config.CacheConfig.GUAVA;

/**
 * @author ENikolskiy.
 */

@CacheConfig(cacheNames = {GUAVA})
public class GuavaCacheProvider<K, V> implements CacheProvider<K, V> {

    private static final String SPEL_KEY = "#key";

    @Override
    @CachePut(key = SPEL_KEY)
    public V put(K key, V value) {
        return value;
    }

    @Override
    @Cacheable(key = SPEL_KEY)
    public V get(K key) {
        return null;
    }

    @Override
    @Cacheable(key = SPEL_KEY)
    @CacheEvict(key = SPEL_KEY)
    public V remove(K key) {
        return null;
    }
}
