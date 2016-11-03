package com.musicforall.history.service.artistHistory;

import com.musicforall.history.model.ArtistHistory;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * Created by Pukho on 19.10.2016.
 */
public interface ArtistHistoryService {

    void record(Map<String, Integer> statistic);

    ArtistHistory get(String name);

    Map<String, Integer> getStatistic(Date begin, Date end);
}
