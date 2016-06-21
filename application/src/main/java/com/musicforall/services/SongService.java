package com.musicforall.services;

import com.musicforall.model.Song;
import com.musicforall.model.Songlist;
import com.musicforall.model.Tag;

import java.util.List;
import java.util.Set;

/**
 * Created by Pukho on 15.06.2016.
 */
public interface SongService {
     void save(Song song, Integer songlistId);
     void save(Song song);
     void delete(Integer songId);
     Song get(Integer id);
     Tag checkExisting(String name);
     Set<Tag> getAllSongTag(Integer songId);
     List<Tag> getAllTags();
     void save(String name, String path);
     void addTag(Integer songId, String nameTag);
}
