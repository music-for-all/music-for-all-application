package com.musicforall.history.handlers;

import com.musicforall.history.handlers.events.PlaylistEvent;
import com.musicforall.history.handlers.events.TrackEvent;
import com.musicforall.history.model.History;
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
        service.record(toHistoryEntity(event));
    }

    @Async
    @EventListener
    public void handlePlaylistEvent(PlaylistEvent event) {
        event.getPlaylistId();
    }

    private History toHistoryEntity(TrackEvent event) {
        final History history = new History();
        history.setEventType(event.getType());
        history.setTrackId(event.getTrackId());
        history.setUserId(event.getUserId());
        history.setDate(new Date());
        return history;
    }
}
