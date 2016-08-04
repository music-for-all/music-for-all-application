package com.musicforall.services.track;

import com.musicforall.common.dao.Dao;
import com.musicforall.model.*;
import com.musicforall.services.SearchCriteriaFactory;
import com.musicforall.services.user.UserService;
import com.musicforall.util.SecurityUtil;
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

    @Autowired
    private UserService userService;

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
    public boolean like(Integer id) {

        final Track track = get(id);
        if (track == null) {
            return false;
        }

        final User user = userService.get(SecurityUtil.currentUser().getId());
        Like like = new Like();

        like.setUser(user);
        like.setTrack(track);
        track.getLikes().add(like);

        like = dao.save(like);
        return like != null;
    }

    @Override
    public int getLikeCount(Integer id) {
        final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Like.class);
        detachedCriteria.add(Restrictions.eq("track.id", id));
        return dao.getCount(detachedCriteria);
    }
}