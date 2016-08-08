package com.musicforall.history.service;
import java.util.List;
import com.musicforall.history.model.History;
/**
 * Created by Pukho on 05.08.2016.
 */

public interface HistoryService {

    void record(History history);

    List<Integer> getTheMostPopularTrack();

    long getLikeCount(Integer trackId);
}
