package com.musicforall.history.handlers;

import com.musicforall.common.dao.Dao;
import com.musicforall.history.handlers.events.AuditionTrackEvent;
import com.musicforall.history.handlers.events.EventType;
import com.musicforall.history.table.HistoryDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

/**
 * @author IliaNik on 17.07.2016.
 */

public class HistoryEventListener {

    @Autowired
    private Dao dao;

    @EventListener
    public void handleAuditionTrack(AuditionTrackEvent event) {

        final HistoryDB historyDB = new HistoryDB();

        historyDB.setEventType(EventType.TRACK_LISTENED);
        historyDB.setTrackId(event.getTrackId());
        historyDB.setUserId(event.getUserId());
        historyDB.setDate(event.getDate());

        dao.save(historyDB);

    }
}
