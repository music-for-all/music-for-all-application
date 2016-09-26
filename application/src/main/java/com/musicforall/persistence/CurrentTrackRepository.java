package com.musicforall.persistence;

import com.musicforall.model.Track;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ENikolskiy.
 */
@Repository
public class CurrentTrackRepository implements KeyValueRepository<Integer, Track> {
    private final Map<Integer, Track> repo = new ConcurrentHashMap<>();

    @Override
    public void put(Integer userId, Track track) {
        repo.put(userId, track);
    }

    @Override
    public Track get(Integer userId) {
        return repo.get(userId);
    }

    @Override
    public Track remove(Integer userId) {
        return repo.remove(userId);
    }
}
