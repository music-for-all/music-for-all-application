package com.musicforall.history.service.history;

import com.musicforall.history.model.History;

import java.util.Collection;
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

    Collection<History> getUsersHistories(Collection<Integer> usersIds);

    Map<Integer, List<History>> getGroupedFollowingHistories(Integer userId);
}
