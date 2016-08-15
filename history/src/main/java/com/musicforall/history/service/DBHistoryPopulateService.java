package com.musicforall.history.service;

import com.musicforall.history.handlers.events.EventType;
import com.musicforall.history.model.History;
import com.musicforall.history.service.history.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Pukho on 12.08.2016.
 */


@Component
public class DBHistoryPopulateService {

    private static final Logger LOG = LoggerFactory.getLogger(DBHistoryPopulateService.class);

    private static final Integer MAX = 10;

    @Autowired
    private HistoryService historyService;

    public void populateTrackListened(List<Integer> tracksIds, Integer userId) {
        LOG.info("going to populate history database with test data");

        recordHistories(tracksIds, userId, EventType.TRACK_LISTENED);
        recordHistories(tracksIds, userId, EventType.TRACK_LIKED);
    }

    private void recordHistories(List<Integer> tracksIds, Integer userId, EventType eventType) {
        final Random rnd = new Random();
        tracksIds.stream()
                .flatMap(t -> {
                    final int listened = rnd.nextInt(MAX);
                    return IntStream.range(0, listened)
                            .mapToObj(i -> new History(t, new Date(), userId, eventType))
                            .collect(Collectors.toList()).stream();
                })
                .forEach(historyService::record);
    }
}

