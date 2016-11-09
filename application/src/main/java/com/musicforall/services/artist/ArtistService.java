package com.musicforall.services.artist;

import com.musicforall.model.Artist;
import com.musicforall.model.SearchArtistRequest;

import java.util.Collection;
import java.util.List;

/**
 * Created by Pavel Podgorniy on 8/19/2016.
 */
public interface ArtistService {

    Artist save(Artist artist);

    Collection<Artist> saveAll(Collection<Artist> artists);

    void delete(Integer artistId);

    Artist get(Integer artistId);

    List<Artist> getAllArtists();

    List<Artist> getAllLike(SearchArtistRequest searchCriteria);
}
