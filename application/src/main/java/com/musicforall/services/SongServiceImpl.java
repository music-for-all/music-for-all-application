package com.musicforall.services;

import com.musicforall.common.dao.Dao;
import com.musicforall.model.Song;
import com.musicforall.model.Songlist;
import com.musicforall.model.Tag;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Pukho on 15.06.2016.
 */
@Service("songService")
@Transactional
public class SongServiceImpl implements SongService {

    @Autowired Dao dao;

    @Autowired
    private SonglistService songlistService;

    @Override
    public void save(String path, Integer songlistId) {
        Songlist songlist = dao.get(Songlist.class, songlistId);
        Song song = new Song(path, songlist.getName());

        songlistService.addSong(song, songlist);

        dao.save(song);
    }

    @Override
    public void delete(Integer songId) {
        Song song = dao.get(Song.class, songId);
        dao.delete(song);
    }

    @Override
    public Song get(Integer id) {
        return dao.get(Song.class, id);
    }
}
