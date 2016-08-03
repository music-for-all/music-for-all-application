package com.musicforall.history.handlers;

import com.musicforall.history.handlers.events.EventType;
import com.musicforall.history.handlers.events.TrackLikedEvent;
import com.musicforall.history.handlers.events.TrackListenedEvent;
import com.musicforall.history.model.History;
import com.musicforall.history.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author IliaNik on 17.07.2016.
 */
@Component
public class HistoryEventListener {

    @Autowired
    private HistoryService service;

    @EventListener
    public void handleTrackListened(TrackListenedEvent event) {
        service.record(toHistoryEntity(event));
    }

    @EventListener
    public void handleTrackLiked(TrackLikedEvent event) {
        service.record(toHistoryEntity(event));
    }

    private History toHistoryEntity(TrackLikedEvent event) {
        final History history = new History();
        history.setEventType(EventType.TRACK_LIKED);
        history.setTrackId(event.getTrackId());
        history.setUserId(event.getUserId());
        history.setDate(event.getDate());
        return history;
    }

    private History toHistoryEntity(TrackListenedEvent event) {
        final History history = new History();
        history.setEventType(EventType.TRACK_LISTENED);
        history.setTrackId(event.getTrackId());
        history.setUserId(event.getUserId());
        history.setDate(new Date());
        return history;
    }
}
