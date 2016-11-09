package com.musicforall.cache;

import com.musicforall.common.cache.CacheProvider;
import com.musicforall.model.Track;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import static com.musicforall.config.CacheConfig.STREAM;

/**
 * @author IliaNik on 20.10.2016.
 */

@CacheConfig(cacheNames = STREAM)
public class StreamCacheProvider implements CacheProvider<Integer, Track> {

    private static final String SPEL_KEY = "#key";

    @Override
    @CachePut(key = SPEL_KEY)
    public Track put(Integer key, Track value) {
        return value;
    }

    @Override
    @Cacheable(key = SPEL_KEY)
    public Track get(Integer key) {
        return null;
    }

    @Override
    @Cacheable(key = SPEL_KEY)
    @CacheEvict(key = SPEL_KEY)
    public Track remove(Integer key) {
        return null;
    }
}
