package com.musicforall.history.handlers;

import com.musicforall.history.handlers.events.PlaylistEvent;
import com.musicforall.history.handlers.events.TrackEvent;
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
        return PlaylistHistory.create()
                .eventType(event.getType())
                .playlistId(event.getPlaylistId())
                .eventType(event.getType())
                .userId(event.getUserId())
                .date(new Date())
                .playlistName(event.getPlaylistName()).get();
    }

    private History toHistory(TrackEvent event) {
        return TrackHistory.create()
                .eventType(event.getType())
                .date(new Date())
                .userId(event.getUserId())
                .trackId(event.getTrackId())
                .trackName(event.getTrackName())
                .playlistId(event.getPlaylistId())
                .artistName(event.getArtistName()).get();
    }
}
