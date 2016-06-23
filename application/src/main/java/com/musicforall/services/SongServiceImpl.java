package com.musicforall.services;

import com.musicforall.common.dao.Dao;
import com.musicforall.model.Song;
import com.musicforall.model.Songlist;
import com.musicforall.model.Tag;
import com.musicforall.web.IndexController;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOG = LoggerFactory.getLogger(SongService.class);
    @Autowired Dao dao;

    @Autowired
    private SonglistService songlistService;

    @Autowired
    private TagService tagService;

    @Override
    public void save(Song song, Integer songlistId) {
        Songlist songlist = dao.get(Songlist.class, songlistId);
        songlistService.addSong(song, songlist);
        if (song.getTags()!= null) this.save(song);
        else dao.save(song);
    }

    @Override
    public void save(Song song) {
        if (song.getTags()!=null){
            for (Tag tag:
                 song.getTags()) {
                dao.save(tag);
            }
        }
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
