package com.musicforall.services.playlist;

import com.musicforall.model.Playlist;
import com.musicforall.model.Track;

import java.util.Set;

/**
 * @author Pukho on 15.06.2016.
 */
public interface PlaylistService {

    /**
     * Creates a new playlist with the given name.
     * @param playlistName the name of the new playlist
     * @return an instance of the created Playlist
     */
    Playlist save(String playlistName);

    /**
     * Saves the given playlist in the database.
     * @param playlist the playlist to be saved
     * @return an instance of the saved Playlist
     */
    Playlist save(Playlist playlist);

    /**
     * Retrieves all tracks in a playlist with the given id.
     * @param playlistId the id of the playlist
     * @return a collection of tracks
     */
    Set<Track> getTracks(Integer playlistId);

    /**
     * Removes a playlist with the given id from the database.
     * @param playlistId the id of the playlist to be deleted
     */
    void delete(Integer playlistId);

    /**
     * Adds a track with the given id to a playlist with the given id.
     * @param playlistId the id of the playlist
     * @param trackId the id of the track
     */
    void addTrack(Integer playlistId, Integer trackId);

    /**
     * Adds a provided collection of tracks to a playlist with the given id.
     * @param playlistId the id of the playlist
     * @param tracks the collection of tracks
     */
    void addTracks(Integer playlistId, Set<Track> tracks);

    /**
     * Removes a track with the given id from a playlist with the given id.
     * @param playlistId the id of the playlist
     * @param trackId the id of the track
     */
    void removeTrack(Integer playlistId, Integer trackId);

    /**
     * Retrieves all playlists of a user with the given id.
     * @param userId the id of the user
     * @return a collection of playlists
     */
    Set<Playlist> getAllUserPlaylists(Integer userId);

    /**
     * Retrieves a playlist with the given id.
     * @param playlistId the id of the playlist
     * @return the retrieved playlist
     */
    Playlist get(Integer playlistId);
}
