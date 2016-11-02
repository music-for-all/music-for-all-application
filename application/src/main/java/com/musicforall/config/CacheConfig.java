package com.musicforall.config;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author ENikolskiy.
 */
@EnableCaching
@Configuration
public class CacheConfig {

    public static final String STREAM = "stream";
    public static final String NOTIFICATION = "notification";

    @Bean
    public CacheManager cacheManager() {
        final long maxSize = 100;
        final long expirePeriod = 10;

        final SimpleCacheManager cacheManager = new SimpleCacheManager();
        final GuavaCache streamCache = new GuavaCache(STREAM, CacheBuilder.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expirePeriod, TimeUnit.MINUTES).build());
        final GuavaCache notificationCache = new GuavaCache(NOTIFICATION, CacheBuilder.newBuilder().build());

        cacheManager.setCaches(Arrays.asList(streamCache, notificationCache));
        return cacheManager;
    }
}
