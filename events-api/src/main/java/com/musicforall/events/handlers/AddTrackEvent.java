package com.musicforall.events.handlers;

import java.util.Date;

/**
 * @author IliaNik on 17.07.2016.
 */
public class AddTrackEvent {

    private final EventType eventType = EventType.TRACKADDED;

    private int track_id;
    private int user_id;
    private Date date;

    public static class Builder {

        private int track_id = 0;
        private int user_id = 0;
        private Date date = new Date();


        public Builder trackID(int val) {
            track_id = val;
            return this;
        }

        public Builder userID(int val) {
            user_id = val;
            return this;
        }

        public Builder date(Date val) {
            date = val;
            return this;
        }


        public AddTrackEvent build() {
            return new AddTrackEvent(this);
        }
    }

    private AddTrackEvent(Builder builder) {
        track_id = builder.track_id;
        user_id = builder.user_id;
        date = builder.date;
    }
}
