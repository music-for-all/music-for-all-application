package com.musicforall.services.stream;

import com.musicforall.model.Track;

import java.util.Collection;
import java.util.Map;

/**
 * @author ENikolskiy.
 */
public interface StreamService {

    void start(Integer userId, Integer trackId);

    void stop(Integer userId);

    void publish(Integer userId, boolean toPublish);

    Map<Integer, Track> getGroupedPublicStreams(Collection<Integer> ids);
}
