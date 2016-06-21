package com.musicforall.services;

import com.musicforall.model.Song;
import com.musicforall.model.Songlist;
import com.musicforall.model.Tag;

import java.util.Set;

/**
 * Created by Pukho on 15.06.2016.
 */
public interface SongService {
     void save(String path, Integer SonglistId);
     void delete(Integer songId);
     Song get(Integer id);
}
