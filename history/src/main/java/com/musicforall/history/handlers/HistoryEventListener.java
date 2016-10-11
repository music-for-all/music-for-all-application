package com.musicforall.history.handlers;

import com.musicforall.history.handlers.events.PlaylistEvent;
import com.musicforall.history.handlers.events.PlaylistEventType;
import com.musicforall.history.handlers.events.TrackEvent;
import com.musicforall.history.handlers.events.TrackEventType;
import com.musicforall.history.model.History;
import com.musicforall.history.model.PlaylistHistory;
import com.musicforall.history.model.TrackHistory;
import com.musicforall.history.service.history.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author IliaNik on 17.07.2016.
 */
@Component
public class HistoryEventListener {

    @Autowired
    private HistoryService service;

    @Async
    @EventListener
    public void handleTrackEvent(TrackEvent event) {
        service.record(toHistory(event));
    }

    @Async
    @EventListener
    public void handlePlaylistEvent(PlaylistEvent event) {
        service.record(toHistory(event));
    }


    private History toHistory(PlaylistEvent event) {
        final PlaylistHistory history = new PlaylistHistory();
        history.setEventType((PlaylistEventType) event.getType());
        history.setPlaylistId(event.getPlaylistId());
        history.setUserId(event.getUserId());
        history.setDate(new Date());
        return history;
    }

    private TrackHistory toHistory(TrackEvent event) {
        final TrackHistory history = new TrackHistory();
        history.setEventType((TrackEventType) event.getType());
        history.setTrackId(event.getTrackId());
        history.setUserId(event.getUserId());
        history.setDate(new Date());
        return history;
    }
}
