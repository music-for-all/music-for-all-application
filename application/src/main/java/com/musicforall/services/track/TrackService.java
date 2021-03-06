package com.musicforall.services.track;

import com.musicforall.model.SearchTrackRequest;
import com.musicforall.model.Tag;
import com.musicforall.model.Track;

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

    Collection<Track> getAllByIds(Collection<Integer> ids);

    void addTags(Integer trackId, Set<Tag> tags);

    List<Track> getAllByName(String name);

    List<Track> getAllLike(SearchTrackRequest searchCriteria);

    List<Track> findAll();

    Collection<Track> getArtistMostPopularTracks(String artistName);

    Collection<String> getArtistMostPopularAlbums(String artistName);
}
