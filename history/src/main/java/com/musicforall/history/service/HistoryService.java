package com.musicforall.history.service;

import com.musicforall.common.dao.Dao;
import com.musicforall.history.handlers.events.EventType;
import com.musicforall.history.model.History;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kgavrylchenko on 25.07.16.
 */
@Service
@Transactional
public class HistoryService {

    @Autowired
    private Dao dao;

    public void record(History history) {
        dao.save(history);
    }

    public History getBy(final DetachedCriteria criteria) {
        return dao.getBy(criteria);
    }

    /**
     * Retrieves the number of likes of a track with the given id.
     * @param trackId the id of the track
     * @return the number of likes
     */
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