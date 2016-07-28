package com.musicforall.services.track;

import com.musicforall.model.Tag;
import com.musicforall.model.Track;
import com.musicforall.model.TrackSearchCriteria;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by Pukho on 15.06.2016.
 */
public interface TrackService {

    Track save(Track track);

    Collection<Track> saveAll(Collection<Track> tracks);

    void delete(Integer trackId);

    Track get(Integer id);

    void addTags(Integer trackId, Set<Tag> tags);

    List<Track> getAllByName(String name);

    List<Track> getAllLike(TrackSearchCriteria searchCriteria);

    List<Track> findAll();
}
