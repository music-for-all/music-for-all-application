package com.musicforall.history.handlers;

import com.musicforall.history.handlers.events.EventType;
import com.musicforall.history.handlers.events.TrackListenedEvent;
import com.musicforall.history.service.HistoryService;
import com.musicforall.history.model.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author IliaNik on 17.07.2016.
 */
@Component
public class HistoryEventListener {

    @Autowired
    private HistoryService service;

    @EventListener
    public void handleAuditionTrack(TrackListenedEvent event) {
        service.record(toHistoryEntity(event));
    }

    private History toHistoryEntity(TrackListenedEvent event) {
        History history = new History();
        history.setEventType(EventType.TRACK_LISTENED);
        history.setTrackId(event.getTrackId());
        history.setUserId(event.getUserId());
        history.setDate(event.getDate());
        return history;
    }
}
