package com.musicforall.history.service.history;

import com.musicforall.history.handlers.events.EventType;
import com.musicforall.history.model.History;

import java.util.Collection;
import java.util.List;

/**
 * Created by Pukho on 05.08.2016.
 */

public interface HistoryService {

    void record(History history);

    List<Integer> getMostPopularTracks();

    Collection<History> getAllBy(SearchHistoryParams params);

    long getLikeCount(Integer trackId);

    Collection<History> getUsersHistories(Collection<Integer> usersIds);

    Collection<History> getAllForUsers(EventType type, Collection<Integer> usersIds);
}
