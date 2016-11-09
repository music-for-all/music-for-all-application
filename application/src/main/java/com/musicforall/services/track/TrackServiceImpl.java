package com.musicforall.services.track;

import com.musicforall.common.Constants;
import com.musicforall.common.dao.Dao;
import com.musicforall.common.dao.QueryParams;
import com.musicforall.common.query.QueryUtil;
import com.musicforall.history.handlers.events.EventType;
import com.musicforall.history.model.History;
import com.musicforall.model.SearchTrackRequest;
import com.musicforall.model.Tag;
import com.musicforall.model.Track;
import com.musicforall.services.SearchCriteriaFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by Pukho on 15.06.2016.
 */
@Service("trackService")
@Transactional
public class TrackServiceImpl implements TrackService {

    @Autowired
    private Dao dao;

    @Override
    public Track save(Track track) {
        return dao.save(track);
    }

    @Override
    public Collection<Track> saveAll(Collection<Track> tracks) {
        return dao.saveAll(tracks);
    }

    @Override
    public void delete(Integer trackId) {
        final Track track = dao.get(Track.class, trackId);
        dao.delete(track);
    }

    @Override
    public Track get(Integer id) {
        return dao.get(Track.class, id);
    }

    @Override
    public void addTags(Integer trackId, Set<Tag> tags) {
        final Track track = get(trackId);
        track.addTags(tags);
        save(track);
    }

    @Override
    public List<Track> getAllByName(String trackName) {
        final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Track.class)
                .add(Restrictions.like(Constants.NAME, QueryUtil.like(trackName)).ignoreCase());
        return dao.getAllBy(detachedCriteria);
    }

    @Override
    public List<Track> getAllLike(SearchTrackRequest searchCriteria) {
        final DetachedCriteria detachedCriteria =
                SearchCriteriaFactory.createTrackSearchCriteria(searchCriteria);
        return dao.getAllBy(detachedCriteria);
    }

    @Override
    public Collection<Track> getAllByIds(Collection<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        final Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        return dao.getAllByNamedQuery(Track.class, Track.ALL_BY_ID_QUERY, params);
    }

    @Override
    public List<Track> findAll() {
        return dao.all(Track.class);
    }

    @Override
    public Collection<Track> getArtistMostPopularTracks(String artistName) {
        final int count = 10;
        final int offset = 0;

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("artistName", artistName);
        List<Integer> trackIds = dao.getAllByNamedQuery(Integer.class, Track.ALL_BY_ARTIST_QUERY,
                parameters);

        parameters.put("trackIds", trackIds);
        parameters.put("eventType", EventType.TRACK_LISTENED);
        trackIds = dao.getAllByNamedQuery(Integer.class, History.POPULAR_TRACKS_BY_ID_QUERY,
                parameters, new QueryParams(count, offset));
        return getAllByIds(trackIds);
    }

    @Override
    public Collection<String> getArtistMostPopularAlbums(String artistName) {
        final int count = 10;
        final int offset = 0;

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("artistName", artistName);
        List<Integer> trackIds = dao.getAllByNamedQuery(Integer.class, Track.ALL_BY_ARTIST_QUERY,
                parameters);

        parameters.put("trackIds", trackIds);
        parameters.put("eventType", EventType.TRACK_LISTENED);
        trackIds = dao.getAllByNamedQuery(Integer.class, History.POPULAR_TRACKS_BY_ID_QUERY,
                parameters);

        parameters.put("trackIds", trackIds);
        final List<String> albums = dao.getAllByNamedQuery(String.class, Track.TOP_ALBUMS_QUERY,
                parameters, new QueryParams(count, offset));
        return albums;
    }
}