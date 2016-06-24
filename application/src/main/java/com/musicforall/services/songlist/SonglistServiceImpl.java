package com.musicforall.services.songlist;

import com.musicforall.common.dao.Dao;
import com.musicforall.model.Playlist;
import com.musicforall.model.Track;
import com.musicforall.model.User;
import com.musicforall.services.song.SongService;
import com.musicforall.services.user.UserService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
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
    private SongService songService;
    @Autowired
    private Dao dao;

    @Override
    public Set<Playlist> getAllUserSonglist(Integer userId) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Playlist.class)
                .add(Property.forName("user.id").eq(userId));
        List<Playlist> usersSonglists = dao.getAllBy(detachedCriteria);
        return new HashSet<Playlist>(usersSonglists);
    }

    @Override
    public void save(Integer userId, String songlistName) {
        Playlist songlist = new Playlist();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        songlist.setUser(userService.getByName(userDetails.getUsername()));
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
    public void delete(Integer songlistId) {
        Playlist songlist = dao.get(Playlist.class, songlistId);
        dao.delete(songlist);
    }

    @Override
    public void addTracks(Integer playlistId, Set<Track> tracks) {
        Playlist playlist = dao.get(Playlist.class, playlistId);
        playlist.addTracks(tracks);
        songService.save(tracks);
    }
}
