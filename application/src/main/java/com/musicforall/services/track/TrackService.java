package com.musicforall.services.track;

import com.musicforall.model.Tag;
import com.musicforall.model.Track;

import java.util.Set;

/**
 * Created by Pukho on 15.06.2016.
 */
public interface TrackService {

    void save(Track track);

    void save(Set<Track> tracks);

    void delete(Integer trackId);

    Track get(Integer id);

    void addTags(Integer trackId, Set<Tag> tags);
}
