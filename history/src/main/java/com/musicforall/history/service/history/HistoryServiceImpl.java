package com.musicforall.history.service.history;

import com.musicforall.common.dao.Dao;
import com.musicforall.history.handlers.events.EventType;
import com.musicforall.history.handlers.events.TrackEvent;
import com.musicforall.history.handlers.events.TrackEventType;
import com.musicforall.history.model.History;
import com.musicforall.history.model.TrackHistory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Pukho on 08.08.2016.
 */
@Service("historyService")
@Transactional
public class HistoryServiceImpl implements HistoryService {

    private static final String EVENT_TYPE = "eventType";
    private static final String TRACK_ID = "trackId";

    @Autowired
    private Dao dao;

    @Override
    public void record(final History history) {
        if (history instanceof TrackHistory) {
            TrackHistory historyTrack = (TrackHistory) history;
            if (historyTrack.getEventType() == TrackEventType.TRACK_LIKED) {
                final Collection<History> histories = getAllBy(SearchHistoryParams.create()
                        .eventType(TrackEventType.TRACK_LIKED)
                        .trackId(historyTrack.getTrackId())
                        .userId(historyTrack.getUserId())
                        .get());
                if (histories.isEmpty()) {
                    dao.save(historyTrack);
                }
            }
        } else {
            dao.save(history);
        }
    }

    @Override
    public List<Integer> getTheMostPopularTracks() {
        return null;   //// TODO: 12.10.2016  change this method on new shema
    }

    @Override
    public Collection<History> getAllBy(SearchHistoryParams params) {
        return dao.getAllBy(toDetachedCriteria(params));
    }

    @Override
    public long getLikeCount(Integer trackId) {

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put(TRACK_ID, trackId);
        parameters.put(EVENT_TYPE, TrackEventType.TRACK_LIKED);

        return dao.getByNamedQuery(Long.class, History.TRACK_LIKES_COUNT_QUERY,
                parameters);
    }

    private DetachedCriteria toDetachedCriteria(SearchHistoryParams params) {
        final DetachedCriteria criteria = DetachedCriteria.forClass(History.class);
        if (params.getUserId() != null) {
            criteria.add(Property.forName("userId").eq(params.getUserId()));
        }
        if (params.getEventType() != null) {
            criteria.add(Property.forName(EVENT_TYPE).eq(params.getEventType()));
        }
        if (params.getTrackId() != null) {
            criteria.add(Property.forName(TRACK_ID).eq(params.getTrackId()));
        }
        return criteria;
    }

    @Override
    public Collection<History> getUsersHistories(Collection<Integer> usersIds) {

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("usersIds", usersIds);

        return dao.getAllByNamedQuery(History.class, History.USERS_HISTORIES_QUERY,
                parameters);
    }

    @Override
    public Collection<History> getAllForUsers(EventType type, Collection<Integer> usersIds) {
        return null;
    }

}
