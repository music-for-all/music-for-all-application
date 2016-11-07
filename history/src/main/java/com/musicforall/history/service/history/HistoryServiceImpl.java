package com.musicforall.history.service.history;

import com.musicforall.common.dao.Dao;
import com.musicforall.common.dao.QueryParams;
import com.musicforall.history.handlers.events.EventType;
import com.musicforall.history.model.History;
import org.hibernate.SessionFactory;
import com.musicforall.history.model.TrackHistory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
@Transactional("history_transaction_manager")
public class HistoryServiceImpl implements HistoryService {

    private static final String EVENT_TYPE = "eventType";
    private static final String TRACK_ID = "trackId";

    @Autowired
    private Dao dao;

    @Autowired
    public void setDao(@Autowired @Qualifier("history_session") SessionFactory sessionFactory) {
        dao.setSessionFactory(sessionFactory);
    }

    @Override
    public void record(final History history) {
        if (history.getEventType() == EventType.TRACK_LIKED) {
            TrackHistory trackHistory = (TrackHistory) history;
            final Collection<TrackHistory> histories = getAllBy(SearchHistoryParams.create()
                    .eventType(EventType.TRACK_LIKED)
                    .trackId(trackHistory.getTrackId())
                    .userId(trackHistory.getUserId())
                    .get());
            if (histories.isEmpty()) {
                dao.save(history);
            }
        } else {
            dao.save(history);
        }
    }

    @Override
    public List<Integer> getTheMostPopularTracks() {

        final int count = 10;
        final int offset = 0;

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put(EVENT_TYPE, EventType.TRACK_LISTENED);

        return dao.getAllByNamedQuery(Integer.class, TrackHistory.POPULAR_TRACKS_QUERY,
                parameters, new QueryParams(count, offset));
    }

    @Override
    public Collection<TrackHistory> getAllBy(SearchHistoryParams params) {
        return dao.getAllBy(toDetachedCriteria(params));
    }

    @Override
    public Collection<History> getAllBy(DetachedCriteria criteria) {
        return dao.getAllBy(criteria);
    }

    @Override
    public long getLikeCount(Integer trackId) {

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put(TRACK_ID, trackId);
        parameters.put(EVENT_TYPE, EventType.TRACK_LIKED);

        return dao.getByNamedQuery(Long.class, TrackHistory.TRACK_LIKES_COUNT_QUERY,
                parameters);
    }

    @Override
    public Collection<History> getAllForUsers(EventType type, Collection<Integer> usersIds) {
     /*   final Map<String, Object> params = new HashMap<>();
        params.put("usersIds", usersIds);
        params.put("eventType", type);
        return dao.getAllByNamedQuery(History.class, "all_for_users_by_type", params);
        */return null;
    }

    @Override
    public Collection<History> histories() {
        return null;
     //  return dao.getAllByNamedQuery(TrackHistory.class, History.USERS_HISTORIES_QUERY, null);
    }

    private DetachedCriteria toDetachedCriteria(SearchHistoryParams params) {
        final DetachedCriteria criteria = DetachedCriteria.forClass(TrackHistory.class);
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

       /* final Map<String, Object> parameters = new HashMap<>();
        parameters.put("usersIds", usersIds);

        return dao.getAllByNamedQuery(History.class, History.USERS_HISTORIES_QUERY,
                parameters);*/
       return null;
    }

}
