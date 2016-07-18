package com.musicforall.history.handlers.events;

import com.musicforall.history.table.UsageHistory;

/**
 * @author IliaNik on 18.07.2016.
 */
public interface HistoryEvent {
    UsageHistory getUsageHistory();
}
