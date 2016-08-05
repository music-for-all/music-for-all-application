package com.musicforall.services.track;

import com.musicforall.model.SearchCriteria;
import com.musicforall.model.Tag;
import com.musicforall.model.Track;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by Pukho on 15.06.2016.
 */
public interface TrackService {

    /**
     * Saves the given instance of the Track class to the database.
     * @param track the track to be saved
     * @return a saved Track instance
     */
    Track save(Track track);

    /**
     * Saves the given collection of type Track to the database.
     * @param tracks the tracks to be saved
     * @return a saved collection of type Track
     */
    Collection<Track> saveAll(Collection<Track> tracks);

    /**
     * Deletes a track with a given id from the database.
     * @param trackId the id of the track
     */
    void delete(Integer trackId);

    /**
     * Retrieves the track with the given id.
     * @param id the id of the track
     * @return the retrieved track
     */
    Track get(Integer id);

    /**
     * Adds a collection of tags to the track with the specified id.
     * @param trackId the id of the track
     * @param tags the tags to add
     */
    void addTags(Integer trackId, Set<Tag> tags);

    /**
     * Retrieves a track which has a specified name.
     * @param name the name of a track to search
     * @return the list of retrieved tracks
     */
    List<Track> getAllByName(String name);

    /**
     * Retrieves a track which correspond to the given  search criteria.
     * @param searchCriteria the search criteria
     * @return the list of retrieved tracks
     */
    List<Track> getAllLike(SearchCriteria searchCriteria);

    /**
     * Retrieves all Track records from the database.
     * @return the list of tracks
     */
    List<Track> findAll();
}
