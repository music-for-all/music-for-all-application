package com.musicforall.persistence;

import com.musicforall.model.Track;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * @author ENikolskiy.
 */
@Component
@CacheConfig(cacheNames = {"guava"})
public class CurrentStreamsCache implements KeyValueRepository<Integer, Track> {

    @Override
    @CachePut(key = "#userId")
    public Track put(Integer userId, Track track) {
        return track;
    }

    @Override
    @Cacheable(key = "#userId")
    public Track get(Integer userId) {
        return null;
    }

    @Override
    @CacheEvict(key = "#userId")
    public Track remove(Integer userId) {
        return null;
    }
}
