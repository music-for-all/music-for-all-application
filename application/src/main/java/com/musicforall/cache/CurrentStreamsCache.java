package com.musicforall.cache;

import com.musicforall.common.cache.GuavaCache;
import com.musicforall.model.Track;
import org.springframework.stereotype.Component;

/**
 * @author ENikolskiy.
 */
@Component
public class CurrentStreamsCache extends GuavaCache<Integer, Track> {

}
