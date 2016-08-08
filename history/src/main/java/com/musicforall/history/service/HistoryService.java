package com.musicforall.history.service;

import com.musicforall.history.model.History;

import java.util.List;

/**
 * Created by Pukho on 05.08.2016.
 */
public interface HistoryService {

    void record(History history);

    List<Integer> getTheMostPopularTrack();
}
