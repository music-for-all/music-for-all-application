package com.musicforall.services.artist;

import com.musicforall.model.Artist;
import com.musicforall.model.Tag;

import java.util.Collection;
import java.util.List;

/**
 * Created by Pavel Podgorniy on 8/19/2016.
 */
public interface ArtistService {
    /**
     * Saves the given instance of the Artist class to the database.
     * @param artist the artist to be saved
     * @return a saved Artist instance
     */
    Artist save(Artist artist);

    /**
     * Saves the given collection of artists in the database.
     * @param artists the collection of artists to be saved
     * @return a collection of saved artists
     */
    Collection<Artist> saveAll(Collection<Artist> artists);

    /**
     * Retrieves a Artist with the provided name.
     * @param artist the name of the artist
     * @return an instance of the retrieved Artist
     */
    Artist get(String artist);

    /**
     * Gets a collection of all c recorded in the database.
     * @return a collection of type Artist
     */
    List<Artist> getAllArtists();

}
