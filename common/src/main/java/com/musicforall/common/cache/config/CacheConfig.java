package com.musicforall.common.cache.config;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author ENikolskiy.
 */
@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        final long maxSize = 100;
        final long expirePeriod = 10;

        final GuavaCacheManager cacheManager = new GuavaCacheManager("guava");
        final CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expirePeriod, TimeUnit.MINUTES);

        cacheManager.setCacheBuilder(cacheBuilder);
        return cacheManager;
    }
}
