package com.musicforall.services.songlist;

import com.musicforall.common.dao.Dao;
import com.musicforall.model.Playlist;
import com.musicforall.model.Track;
import com.musicforall.model.User;
import com.musicforall.services.user.UserService;
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
@Service("songlistService")
@Transactional
public class SonglistServiceImpl implements SonglistService {

    @Autowired
    private UserService userService;
    @Autowired
    private Dao dao;

    @Override
    public Set<Playlist> getAllUserSonglist(Integer userId) {

        User user = userService.get(userId);
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Playlist.class)
                .add(Property.forName("user").eq(user));
        List<Playlist> usersSonglists = dao.getAllBy(detachedCriteria);
        return new HashSet<Playlist>(usersSonglists);
    }

    @Override
    public void save(Integer userId, String songlistName) {
        Playlist songlist = new Playlist(); //there is have to be some builder ??
        songlist.setUser(userService.get(userId));
        songlist.setName(songlistName);
        dao.save(songlist);
    }

    @Override
    public void save(Integer userId, Playlist songlist) {
        songlist.setUser(userService.get(userId));
        dao.save(songlist);
    }

    @Override
    public Set<Track> getAllSongsInSonglist(Integer songlistId) {
        Playlist songlist = dao.get(Playlist.class, songlistId);
        return songlist.getTracks();
    }


    @Override
    public void addSong(Track song, Playlist songlist) {
        if (songlist.getTracks() == null)
            songlist.setTracks(new HashSet<>());

        songlist.getTracks().add(song);
        dao.save(songlist);
    }

    @Override
    public void delete(Integer songlistId) {
        Playlist songlist = dao.get(Playlist.class, songlistId);

        dao.delete(songlist);
    }
}
