package com.musicforall.history.handlers;

import com.musicforall.common.dao.Dao;
import com.musicforall.history.handlers.events.HistoryEvent;
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
    public void handleHistoryEvent(HistoryEvent event) {

        final UsageHistory usageHistory = event.getUsageHistory();
        dao.save(usageHistory);

    }
}
