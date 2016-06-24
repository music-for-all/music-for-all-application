package com.musicforall.services.songlist;

import com.musicforall.model.Playlist;
import com.musicforall.model.Track;

import java.util.Set;

/**
 * Created by Pukho on 15.06.2016.
 */
public interface SonglistService {

    void save(Integer userId, String songlistName);

    void save(Integer userId, Playlist songlist);

    Set<Track> getAllSongsInSonglist(Integer songlistId);

    void delete(Integer songlistId);

    void addTracks(Integer playlistId, Set<Track> tracks);

    Set<Playlist> getAllUserSonglist(Integer userId);
}
