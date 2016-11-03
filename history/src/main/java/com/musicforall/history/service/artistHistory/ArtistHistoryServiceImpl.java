package com.musicforall.history.service.artistHistory;

import com.musicforall.common.dao.Dao;
import com.musicforall.history.handlers.events.EventType;
import com.musicforall.history.model.ArtistHistory;
import com.musicforall.history.model.History;
import com.musicforall.history.service.artistHistory.ArtistHistoryService;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Pukho on 19.10.2016.
 */
@Service("artistHistoryService")
@Transactional("history_transaction_manager")
public class ArtistHistoryServiceImpl implements ArtistHistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(ArtistHistoryService.class);

    @Autowired
    private Dao dao;

    @Autowired
    public void setDao(@Autowired @Qualifier("history_session") SessionFactory sessionFactory) {
        dao.setSessionFactory(sessionFactory);
    }
    @Override
    public ArtistHistory get(String name) {
        final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ArtistHistory.class)
                .add(Property.forName("artistName").eq(name));
        return dao.getBy(detachedCriteria);
    }

    @Override
    public void record(Map<String, Integer> statistic) {
        statistic.keySet().stream().map(f -> {
            int number_of_plays = statistic.get(f);
            ArtistHistory artistHistory = this.get(f);
            if (artistHistory != null) {
                artistHistory.setPlaysCount(artistHistory.getPlaysCount() + number_of_plays);
            } else {
                artistHistory = new ArtistHistory(f, number_of_plays);
            }
            return artistHistory;
        }).forEach(dao::save);
    }

    @Override
    public Map<String, Integer> getStatistic(Date begin, Date end) {

        final Map<String, Object> params = new HashMap<>();
        params.put("begin", begin);
        params.put("end", end);
        params.put("eventType", EventType.TRACK_LISTENED);

        Collection<History> histories = dao.getAllByNamedQuery(History.class, History.ALL_HISTORY_BY_TIME, params);
        return
                histories.stream()
                        .collect(Collectors.toConcurrentMap(
                                History::getArtistName, w -> 1, Integer::sum));
    }
}

