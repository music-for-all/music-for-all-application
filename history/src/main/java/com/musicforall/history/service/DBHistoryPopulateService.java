package com.musicforall.history.service;

import com.musicforall.history.handlers.HistoryEventListener;
import com.musicforall.history.handlers.events.EventType;
import com.musicforall.history.handlers.events.TrackLikedEvent;
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

    @Autowired
    private HistoryEventListener historyEventListener;

    public void populateTrackListened(List<Integer> tracksIds, Integer userId) {
        LOG.info("going to populate history database with test data");

        recordHistories(tracksIds, userId, EventType.TRACK_LISTENED);
        recordHistories(tracksIds, userId, EventType.TRACK_LIKED);
    }

    public void populateTrackLikedByFollowedUsers() {

        final int FOLLOWED_USER_ID = 2;
        final int TRACK_1_ID = 1;
        final int TRACK_2_ID = 2;
        final int TRACK_3_ID = 3;

        historyEventListener.handleTrackLiked(new TrackLikedEvent(TRACK_1_ID, FOLLOWED_USER_ID));
        historyEventListener.handleTrackLiked(new TrackLikedEvent(TRACK_1_ID, FOLLOWED_USER_ID));
        historyEventListener.handleTrackLiked(new TrackLikedEvent(TRACK_3_ID, FOLLOWED_USER_ID));
        historyEventListener.handleTrackLiked(new TrackLikedEvent(TRACK_2_ID, FOLLOWED_USER_ID));
        historyEventListener.handleTrackLiked(new TrackLikedEvent(TRACK_2_ID, FOLLOWED_USER_ID));
        historyEventListener.handleTrackLiked(new TrackLikedEvent(TRACK_2_ID, FOLLOWED_USER_ID));
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
                            .mapToObj(i -> new History(t, new Date(), userId, eventType))
                            .collect(Collectors.toList()).stream();
                })
                .forEach(historyService::record);
    }
}

