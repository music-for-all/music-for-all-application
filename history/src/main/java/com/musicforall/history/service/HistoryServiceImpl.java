package com.musicforall.history.service;

import com.musicforall.common.dao.Dao;
import com.musicforall.history.handlers.events.EventType;
import com.musicforall.history.model.History;
import org.hibernate.criterion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        final Integer count = 10;
        final String hql = "select history.trackId" +
                " from History history" +
                " where history.eventType=:trackListened" +
                " group by history.trackId" +
                " order by count(history.trackId) desc";

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("trackListened", EventType.TRACK_LISTENED);

        return dao.getAllBy(hql, parameters, count);
    }

    @Override
    public History getBy(DetachedCriteria detachedCriteria) {
        return dao.getBy(detachedCriteria);
    }

    @Override
    public long getLikeCount(Integer trackId) {

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("trackId", trackId);
        parameters.put("eventType", EventType.TRACK_LIKED);

        final long count = dao.getBy("select count(*) from History history " +
                        "where history.trackId=:trackId and history.eventType=:eventType",
                parameters);
        return count;
    }
}
