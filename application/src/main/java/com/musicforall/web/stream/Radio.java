package com.musicforall.web.stream;

import com.musicforall.model.Track;

/**
 * @author ENikolskiy.
 */
public interface Radio {
    void stream(Track track, int partId);

    void stream(Integer userId, Track track, int partId);
}
