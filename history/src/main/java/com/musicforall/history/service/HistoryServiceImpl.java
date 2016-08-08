package com.musicforall.history.service;

import com.musicforall.common.dao.Dao;
import com.musicforall.history.model.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Pukho on 08.08.2016.
 */
@Service("historyService")
@Transactional
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    private Dao dao;

    @Override
    public void record(History history) {
        dao.save(history);
    }

    @Override
    public List<Integer> getTheMostPopularTrack() {
        String hql = "select history.trackId" +
                " from History history" +
                " where history.eventType='TRACK_LISTENED'" +
                " group by history.trackId" +
                " order by count(history.trackId) desc";

        return dao.getAllBy(hql, null);
    }
}
