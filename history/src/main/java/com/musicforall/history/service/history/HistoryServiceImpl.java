package com.musicforall.history.service.history;

import com.musicforall.common.dao.Dao;
import com.musicforall.common.dao.QueryParams;
import com.musicforall.history.handlers.events.EventType;
import com.musicforall.history.model.History;
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

    @Autowired
    private Dao dao;

    @Override
    public void record(final History history) {
        dao.save(history);
    }

    @Override
    public List<Integer> getTheMostPopularTracks() {

        final int count = 10;
        final int offset = 0;

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("eventType", EventType.TRACK_LISTENED);

        return dao.getAllByNamedQuery(Integer.class, History.POPULAR_TRACKS_QUERY,
                parameters, new QueryParams(count, offset));
    }

    @Override
    public Collection<History> getAllBy(SearchHistoryParams params) {
        return dao.getAllBy(toDetachedCriteria(params));
    }

    @Override
    public long getLikeCount(Integer trackId) {

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("trackId", trackId);
        parameters.put("eventType", EventType.TRACK_LIKED);

        return dao.getByNamedQuery(Long.class, History.TRACK_LIKES_COUNT_QUERY,
                parameters);
    }

    private DetachedCriteria toDetachedCriteria(SearchHistoryParams params) {
        final DetachedCriteria criteria = DetachedCriteria.forClass(History.class);
        if (params.getUserId() != null) {
            criteria.add(Property.forName("userId").eq(params.getUserId()));
        }
        if (params.getEventType() != null) {
            criteria.add(Property.forName("eventType").eq(params.getEventType()));
        }
        if (params.getTrackId() != null) {
            criteria.add(Property.forName("trackId").eq(params.getTrackId()));
        }
        return criteria;
    }

    public Collection<History> getUsersHistories(Collection<Integer> usersIds) {

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("usersIds", usersIds);

        return dao.getAllByNamedQuery(History.class, History.USERS_HISTORIES_QUERY,
                parameters);
    }

}
