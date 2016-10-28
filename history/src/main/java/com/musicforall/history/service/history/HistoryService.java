package com.musicforall.history.service.history;

import com.musicforall.history.handlers.events.EventType;
import com.musicforall.history.model.History;
import org.hibernate.criterion.DetachedCriteria;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Pukho on 05.08.2016.
 */

public interface HistoryService {

    void record(History history);

    List<Integer> getTheMostPopularTracks();

    Collection<History> getAllBy(SearchHistoryParams params);

    long getLikeCount(Integer trackId);

    public Collection<History> getAllBy(DetachedCriteria criteria);

    Collection<History> getUsersHistories(Collection<Integer> usersIds);

    Collection<History> getAllForUsers(EventType type, Collection<Integer> usersIds);
}
