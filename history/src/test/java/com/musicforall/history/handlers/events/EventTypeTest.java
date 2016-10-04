package com.musicforall.history.handlers.events;

import org.junit.Test;

import static com.musicforall.history.handlers.events.EventType.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EventTypeTest {

    @Test
    public void testIn() throws Exception {
        final boolean in = TRACK_ADDED.in(TRACK_ADDED, TRACK_DELETED);
        assertTrue(in);

        final boolean notIn = TRACK_LIKED.in(TRACK_ADDED, TRACK_DELETED);
        assertFalse(notIn);
    }
}