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

import java.util.Set;

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
    private TagService tagService;

    @Override
    public void save(Track track) {
        if (track.getTags() != null){
            tagService.save(track.getTags());
        }
        dao.save(track);
    }

    @Override
    public void save(Set<Track> tracks) {
        for (Track track:
             tracks) {
            this.save(track);
        }
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

    @Override
    public void addTags(Integer trackId, Set<Tag> tags) {
        Track track = get(trackId);

        track.addTags(tags);
        tagService.save(tags);
        dao.save(track);
    }

}
