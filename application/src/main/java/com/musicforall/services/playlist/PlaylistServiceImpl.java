package com.musicforall.services.playlist;

import com.musicforall.common.dao.Dao;
import com.musicforall.model.Playlist;
import com.musicforall.model.Track;
import com.musicforall.services.track.TrackService;
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
    private TrackService trackService;
    @Autowired
    private Dao dao;

    @Override
    public Set<Playlist> getAllUserPlaylist(Integer userId) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Playlist.class)
                .add(Property.forName("user.id").eq(userId));
        List<Playlist> usersPlaylists = dao.getAllBy(detachedCriteria);
        return new HashSet<Playlist>(usersPlaylists);
    }

    @Override
    public Playlist get(Integer playlistId) {
        return dao.get(Playlist.class, playlistId);
    }

    @Override
    public Integer save(String playlistName) {
        Playlist playlist = new Playlist();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        playlist.setUser(userService.getByName(userDetails.getUsername()));
        playlist.setName(playlistName);
        dao.save(playlist);
        return playlist.getId();
    }

    @Override
    public Set<Track> getAllTracksInPlaylist(Integer playlistId) {
        Playlist playlist = dao.get(Playlist.class, playlistId);
        return playlist.getTracks();
    }

    @Override
    public void delete(Integer playlistId) {
        Playlist playlist = dao.get(Playlist.class, playlistId);
        dao.delete(playlist);
    }

    @Override
    public void addTracks(Integer playlistId, Set<Track> tracks) {
        Playlist playlist = dao.get(Playlist.class, playlistId);
        playlist.addTracks(tracks);
        trackService.save(tracks);
    }
}
