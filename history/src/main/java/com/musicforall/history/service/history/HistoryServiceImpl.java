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
        final String hql = "select history.trackId" +
                " from History history" +
                " where history.eventType=:eventType" +
                " group by history.trackId" +
                " order by count(history.trackId) desc";

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("eventType", EventType.TRACK_LISTENED);

        return dao.getAllBy(hql, parameters, new QueryParams(count, offset));
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

        return dao.getBy("select count(*) from History history " +
                        "where history.trackId=:trackId and history.eventType=:eventType",
                parameters);
    }

    @Override
    public Collection<History> getAllForUsers(EventType type, List<Integer> usersIds) {
        Map<String, Object> params = new HashMap<>();
        params.put("usersIds", usersIds);
        params.put("eventType", type);
        return dao.getAllByNamedQuery(History.class, "all_for_users_by_type", params);
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
}
