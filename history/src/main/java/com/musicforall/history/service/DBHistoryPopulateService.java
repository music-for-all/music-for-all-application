package com.musicforall.history.service;

import com.musicforall.history.handlers.events.EventType;
import com.musicforall.history.model.History;
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

    private static final Integer MAX_LISTENED = 10;

    private static final Logger LOG = LoggerFactory.getLogger(DBHistoryPopulateService.class);

    @Autowired
    private HistoryService historyService;

    public void populateTrackListened(List<Integer> tracks, Integer userId) {

        LOG.info("going to populate history database with test data");

        final Random rnd = new Random();
        tracks.stream()
                .flatMap(t -> {
                    int listened =  rnd.nextInt(MAX_LISTENED);
                    return IntStream.range(0, listened)
                            .mapToObj(i -> new History(t, new Date(), userId, EventType.TRACK_LISTENED))
                            .collect(Collectors.toList()).stream();
                })
                .forEach(historyService::record);
    }
}

