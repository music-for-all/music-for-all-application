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

    private static final int MAX = 10;

    @Autowired
    private HistoryService historyService;

    public void populateTrackListened(List<Integer> tracksIds, Integer userId) {
        LOG.info("going to populate history database with test data");

        recordHistories(tracksIds, userId, EventType.TRACK_LISTENED);
        recordHistoriesTracksLiked(tracksIds, userId);
    }

    public void populateTrackLikedByFollowedUsers(List<Integer> trackIds, Integer userId) {
        recordHistoriesTracksLiked(trackIds, userId);
    }

    private void recordHistories(List<Integer> tracksIds, Integer userId, EventType eventType) {
        final Random rnd = new Random();
        tracksIds.stream()
                .flatMap(t -> {
                    int listened = 0;
                    while (listened == 0) {
                        listened = rnd.nextInt(MAX);
                    }
                    return IntStream.range(0, listened)
                            .mapToObj(i -> new History(t, null, new Date(), userId, eventType))
                            .collect(Collectors.toList()).stream();
                })
                .forEach(historyService::record);
    }

    private void recordHistoriesTracksLiked(List<Integer> tracksIds, Integer userId) {
        tracksIds.stream()
                .map(t -> new History(t, null, new Date(), userId, EventType.TRACK_LIKED))
                .forEach(historyService::record);
    }

    public void recordArtistHistories(List<String> artistNames, Long date, Integer userId, EventType eventType) {
        final Random rnd = new Random();
        artistNames.stream()
                .flatMap(t -> {
                    int listened = 0;
                    while (listened == 0) {
                        listened = rnd.nextInt(MAX);
                    }
                    return IntStream.range(0, listened)
                            .mapToObj(i -> History.create()
                                    .artistName(t)
                                    .eventType(eventType)
                                    .date(new Date(date)).userId(userId).get())
                            .collect(Collectors.toList()).stream();
                })
                .forEach(historyService::record);
    }
}
