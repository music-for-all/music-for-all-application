package com.musicforall.services.playlist;

import com.musicforall.model.Playlist;
import com.musicforall.model.Track;

import java.util.Set;

/**
 * @author Pukho on 15.06.2016.
 */
public interface PlaylistService {

    Playlist save(String playlistName);

    Playlist save(Playlist playlist);

    Set<Track> getAllTracksInPlaylist(Integer playlistId);

    void delete(Integer playlistId);

    void addTracks(Integer playlistId, Set<Track> tracks);

    Set<Playlist> getAllUserPlaylists(Integer userId);

    Playlist get(Integer playlistId);

    void addTrack(Integer playlistId, Integer trackId);

    void removeTrack(Integer playlistId, Integer trackId);
}
