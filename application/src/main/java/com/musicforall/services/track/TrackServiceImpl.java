package com.musicforall.services.track;

import com.musicforall.common.dao.Dao;
import com.musicforall.model.SearchTrackRequest;
import com.musicforall.model.Tag;
import com.musicforall.model.Track;
import com.musicforall.model.User;
import com.musicforall.services.SearchCriteriaFactory;
import com.musicforall.services.follower.FollowerService;
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

    @Autowired
    private FollowerService followerService;

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
    public List<Track> getAllLike(SearchTrackRequest searchCriteria) {

        final DetachedCriteria detachedCriteria =
                SearchCriteriaFactory.createTrackSearchCriteria(searchCriteria);
        return dao.getAllBy(detachedCriteria);
    }

    @Override
    public List<Track> findAll() {
        return dao.all(Track.class);
    }

    @Override
    public List<Track> getRecommendedTracks() {

        final User user = SecurityUtil.currentUser();

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("userId", user.getId());
        parameters.put("followedUserIds", followerService.getFollowingId(user.getId()));

        final List<Track> tracks = dao.getAllBy("select t from Track t where t.id in " +
                "(select h.trackId from History h " +
                "where h.eventType = 'TRACK_LIKED' and h.userId in (:followedUserIds) " +
                "group by h.trackId order by count(*) desc)",
                parameters);

        /* Remove a recommended track if it is already in one of the user's playlists. */
        for (final Iterator<Track> trackIter = tracks.iterator(); trackIter.hasNext();) {

            final Track track = trackIter.next();
            parameters.put("trackId", track.getId());
            final Long num = dao.getBy("select count(*) from Playlist playlist " +
                            "join playlist.tracks track " +
                            "where playlist.user.id = :userId and track.id = :trackId",
                    parameters);
            if (num > 0) {
                trackIter.remove();
            }
        }
        return tracks;
    }
}