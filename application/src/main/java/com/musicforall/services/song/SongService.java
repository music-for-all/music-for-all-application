package com.musicforall.services.song;

import com.musicforall.model.Tag;
import com.musicforall.model.Track;

import java.util.Set;

/**
 * Created by Pukho on 15.06.2016.
 */
public interface SongService {

    void save(Track track);

    void save(Set<Track> tracks);

    void delete(Integer songId);

    Track get(Integer id);

    void addTags(Integer trackId, Set<Tag> tags);
}
