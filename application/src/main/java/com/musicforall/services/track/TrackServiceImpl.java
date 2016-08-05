package com.musicforall.services.track;

import com.musicforall.common.dao.Dao;
import com.musicforall.model.SearchCriteria;
import com.musicforall.model.Tag;
import com.musicforall.model.Track;
import com.musicforall.services.SearchCriteriaFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
                .add(Restrictions.like("name", "%" + trackName + "%").ignoreCase());
        return dao.getAllBy(detachedCriteria);
    }

    @Override
    public List<Track> getAllLike(SearchCriteria searchCriteria) {

        final DetachedCriteria detachedCriteria =
                SearchCriteriaFactory.buildTrackSearchCriteria(searchCriteria);
        return dao.getAllBy(detachedCriteria);
    }

    @Override
    public List<Track> findAll() {
        return dao.all(Track.class);
    }

    @Override
    public List<Track> getTheMostPopular() {
        String hql = "select history.trackId" +
                " from History history" +
                " where history.eventType='TRACK_LISTENED'" +
                " group by history.trackId" +
                " order by count(history.trackId) desc";

        return dao.getAllBy(hql, null).
                stream().limit(10).map(f -> (Integer)f).map(this::get).collect(Collectors.toList());
    }
}