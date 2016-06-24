package com.musicforall.services.song;

import com.musicforall.model.Track;

/**
 * Created by Pukho on 15.06.2016.
 */
public interface SongService {
    void save(Track song, Integer songlistId);

    void save(Track song);

    void delete(Integer songId);

    Track get(Integer id);
}
