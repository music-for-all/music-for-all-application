package com.musicforall.history.service.history;

import com.musicforall.history.handlers.events.EventType;

/**
 * Created by kgavrylchenko on 15.08.16.
 */
public final class SearchHistoryParams {

    private final EventType eventType;
    private final Integer userId;
    private final Integer trackId;

    private SearchHistoryParams(EventType eventType, Integer userId, Integer trackId) {
        this.eventType = eventType;
        this.userId = userId;
        this.trackId = trackId;
    }

    public static Builder create() {
        return new Builder();
    }

    public EventType getEventType() {
        return eventType;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getTrackId() {
        return trackId;
    }

    public static final class Builder {
        private EventType eventType;
        private Integer userId;
        private Integer trackId;

        private Builder() {
        }

        public Builder eventType(EventType eventType) {
            this.eventType = eventType;
            return this;
        }

        public Builder userId(Integer userId) {
            this.userId = userId;
            return this;
        }

        public Builder trackId(Integer trackId) {
            this.trackId = trackId;
            return this;
        }

        public SearchHistoryParams get() {
            return new SearchHistoryParams(eventType, userId, trackId);
        }
    }
}
