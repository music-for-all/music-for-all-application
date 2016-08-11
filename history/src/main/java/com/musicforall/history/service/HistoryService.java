package com.musicforall.history.service;
import java.util.List;
import com.musicforall.history.model.History;
import org.hibernate.criterion.DetachedCriteria;

/**
 * Created by Pukho on 05.08.2016.
 */

public interface HistoryService {

    void record(History history);

    List<Integer> getTheMostPopularTracks();

    History getBy(DetachedCriteria detachedCriteria);

    long getLikeCount(Integer trackId);
}
