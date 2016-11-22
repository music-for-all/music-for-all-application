package com.musicforall.services.playlist;

import com.musicforall.common.dao.Dao;
import com.musicforall.model.Playlist;
import com.musicforall.model.Track;
import com.musicforall.model.user.User;
import com.musicforall.services.track.TrackService;
import com.musicforall.util.SecurityUtil;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by Pukho on 15.06.2016.
 */
@Service("playlistService")
@Transactional
public class PlaylistServiceImpl implements PlaylistService {

    @Autowired
    private Dao dao;
    @Autowired
    private TrackService trackService;

    @Override
    public Set<Playlist> getAllUserPlaylists(Integer userId) {
        final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Playlist.class)
                .add(Property.forName("user.id").eq(userId));
        final List<Playlist> usersPlaylists = dao.getAllBy(detachedCriteria);
        return new HashSet<>(usersPlaylists);
    }

    @Override
    public Playlist get(Integer playlistId) {
        return dao.get(Playlist.class, playlistId);
    }

    @Override
    public Playlist save(String playlistName) {
        final Playlist playlist = new Playlist();
        final User user = SecurityUtil.currentUser();

        playlist.setUser(user);
        playlist.setName(playlistName);
        return save(playlist);
    }

    @Override
    public Playlist save(Playlist playlist) {
        return dao.save(playlist);
    }

    @Override
    public Set<Track> getAllTracksInPlaylist(Integer playlistId) {
        final Playlist playlist = dao.get(Playlist.class, playlistId);
        return playlist.getTracks();
    }

    @Override
    public Collection<Playlist> getAllByIds(Collection<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        final Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        return dao.getAllByNamedQuery(Playlist.class, Playlist.ALL_BY_ID_QUERY, params);
    }

    @Override
    public void delete(Integer playlistId) {
        final Playlist playlist = dao.get(Playlist.class, playlistId);
        dao.delete(playlist);
    }

    @Override
    public void addTrack(Integer playlistId, Integer trackId) {
        final Playlist playlist = dao.get(Playlist.class, playlistId);
        final Track track = dao.get(Track.class, trackId);

        playlist.getTracks().add(track);
        save(playlist);
    }

    @Override
    public void addTracks(Integer playlistId, Collection<Integer> tracksIds) {
        final Playlist playlist = dao.get(Playlist.class, playlistId);
        final Collection<Track> tracks = trackService.getAllByIds(tracksIds);

        playlist.addTracks(new HashSet<>(tracks));
        save(playlist);
    }

    @Override
    public void removeTrack(Integer playlistId, Integer trackId) {
        final Playlist playlist = dao.get(Playlist.class, playlistId);
        playlist.getTracks().removeIf(track -> {
            if (Objects.equals(track.getId(), trackId)) {
                return true;
            }
            return false;
        });
        save(playlist);
    }
}
