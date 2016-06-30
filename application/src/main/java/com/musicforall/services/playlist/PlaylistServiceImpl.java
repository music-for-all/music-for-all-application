package com.musicforall.services.playlist;

import com.musicforall.common.dao.Dao;
import com.musicforall.model.Playlist;
import com.musicforall.model.Track;
import com.musicforall.services.user.UserService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Pukho on 15.06.2016.
 */
@Service("playlistService")
@Transactional
public class PlaylistServiceImpl implements PlaylistService {

    @Autowired
    private UserService userService;

    @Autowired
    private Dao dao;

    @Override
    public Set<Playlist> getAllUserPlaylist(Integer userId) {
        final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Playlist.class)
                .add(Property.forName("user.id").eq(userId));
        final List<Playlist> usersPlaylists = dao.getAllBy(detachedCriteria);
        return new HashSet<Playlist>(usersPlaylists);
    }

    @Override
    public Playlist get(Integer playlistId) {
        return dao.get(Playlist.class, playlistId);
    }

    @Override
    public Playlist save(String playlistName) {
        final Playlist playlist = new Playlist();

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails userDetails = (UserDetails) auth.getPrincipal();

        playlist.setUser(userService.getByName(userDetails.getUsername()));
        playlist.setName(playlistName);
        return save(playlist);
    }

    @Override
    public Playlist save(Playlist playlist) {
        return dao.save(playlist);
    }

    @Override
    public Set<Track> getAllTracksInPlaylist(Integer playlistId) {
        Playlist playlist = dao.get(Playlist.class, playlistId);
        return playlist.getTracks();
    }

    @Override
    public void delete(Integer playlistId) {
        final Playlist playlist = dao.get(Playlist.class, playlistId);
        dao.delete(playlist);
    }

    @Override
    public void addTracks(Integer playlistId, Set<Track> tracks) {
        final Playlist playlist = dao.get(Playlist.class, playlistId);
        playlist.addTracks(tracks);
        save(playlist);
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }
}
