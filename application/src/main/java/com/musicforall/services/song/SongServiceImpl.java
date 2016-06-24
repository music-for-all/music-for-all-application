package com.musicforall.services.song;

import com.musicforall.common.dao.Dao;
import com.musicforall.model.Playlist;
import com.musicforall.model.Tag;
import com.musicforall.model.Track;
import com.musicforall.services.songlist.SonglistService;
import com.musicforall.services.tag.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Pukho on 15.06.2016.
 */
@Service("songService")
@Transactional
public class SongServiceImpl implements SongService {

    private static final Logger LOG = LoggerFactory.getLogger(SongService.class);
    @Autowired
    Dao dao;

    @Autowired
    private SonglistService songlistService;

    @Autowired
    private TagService tagService;

    @Override
    public void save(Track song, Integer songlistId) {
        Playlist songlist = dao.get(Playlist.class, songlistId);
        songlistService.addSong(song, songlist);
        if (song.getTags() != null) this.save(song);
        else dao.save(song);
    }

    @Override
    public void save(Track song) {
        if (song.getTags() != null) {
            for (Tag tag :
                    song.getTags()) {
                dao.save(tag);
            }
        }
        dao.save(song);
    }

    @Override
    public void delete(Integer songId) {
        Track song = dao.get(Track.class, songId);
        dao.delete(song);
    }

    @Override
    public Track get(Integer id) {
        return dao.get(Track.class, id);
    }

}
