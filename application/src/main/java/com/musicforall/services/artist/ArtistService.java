package com.musicforall.services.artist;

import com.musicforall.model.Artist;

import java.util.Collection;
import java.util.List;

/**
 * Created by Pavel Podgorniy on 8/19/2016.
 */
public interface ArtistService {

    Artist save(Artist artist);

    Collection<Artist> saveAll(Collection<Artist> artists);

    Artist get(String artist);

    List<Artist> getAllArtists();

    List<Artist> getAllLike(String artistName);
}
