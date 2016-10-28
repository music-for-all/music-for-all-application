package com.musicforall.history.service.history;

import com.musicforall.history.handlers.events.EventType;
import com.musicforall.history.model.History;
import com.musicforall.history.service.DBHistoryPopulateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Created by Pukho on 25.10.2016.
 */
@Component
public class TimeService {
    public static final int TIME_GAP = 300000; //5 min

    private static final Logger LOG = LoggerFactory.getLogger(TimeService.class);

    @Autowired
    private ArtistHistoryService artistService;

    @Scheduled(fixedRate=TIME_GAP)
    public void updateArtistHistoryTable() {
        long currentTime = new Date().getTime();
        artistService.record(artistService.getStatistic(new Date(currentTime-TIME_GAP), new Date()));
    }
}
