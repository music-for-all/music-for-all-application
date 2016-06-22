package com.musicforall.services;

import com.musicforall.common.dao.Dao;
import com.musicforall.model.Song;
import com.musicforall.model.Songlist;
import com.musicforall.model.User;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jca.cci.core.InteractionCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Pukho on 15.06.2016.
 */
@Service("songlistService")
@Transactional
public class SonglistServiceImpl implements SonglistService {

    @Autowired
    private UserService userService;
    @Autowired
    private Dao dao;

    @Override
    public Set<Songlist> getAllUserSonglist(Integer userId) {

        User user = userService.get(userId);
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Songlist.class)
                .add(Property.forName("user").eq(user));
        List<Songlist> usersSonglists = dao.getAllBy(detachedCriteria);
        return new HashSet<Songlist>(usersSonglists);
    }

    @Override
    public void save(Integer userId, String songlistName) {
        Songlist songlist = new Songlist(); //there is have to be some builder ??
        songlist.setUser(userService.get(userId));
        songlist.setName(songlistName);
        dao.save(songlist);
    }

    @Override
    public void save(Integer userId, Songlist songlist) {
        songlist.setUser(userService.get(userId));
        dao.save(songlist);
    }

    @Override
    public Set<Song> getAllSongsInSonglist(Integer songlistId) {
        Songlist songlist = dao.get(Songlist.class, songlistId);
        return songlist.getSongs();
    }


    @Override
    public void addSong(Song song, Songlist songlist) {
        if (songlist.getSongs() == null)
            songlist.setSongs(new HashSet<>());

        songlist.getSongs().add(song);
        dao.save(songlist);
    }

    @Override
    public void delete(Integer songlistId) {
        Songlist songlist = dao.get(Songlist.class, songlistId);

        dao.delete(songlist);
    }
}
