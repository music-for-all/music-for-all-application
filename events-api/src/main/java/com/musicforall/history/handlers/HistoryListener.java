package com.musicforall.history.handlers;

import com.musicforall.common.dao.Dao;
import com.musicforall.history.handlers.events.AuditionTrackEvent;
import com.musicforall.history.handlers.events.EventType;
import com.musicforall.history.table.UsageHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

/**
 * @author IliaNik on 17.07.2016.
 */

public class HistoryListener {

    @Autowired
    private Dao dao;

    @EventListener
    public void handleAuditionTrack(AuditionTrackEvent event) {

        UsageHistory usageHistory = new UsageHistory();

        usageHistory.setEventType(EventType.TRACK_AUDITIONED);
        usageHistory.setTrackId(event.getTrackId());
        usageHistory.setUserId(event.getUserId());
        usageHistory.setDate(event.getDate());

        dao.save(usageHistory);

    }
}
