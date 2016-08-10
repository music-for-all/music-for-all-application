package com.musicforall.history.service;

import com.musicforall.history.handlers.HistoryEventListener;
import com.musicforall.history.handlers.events.EventType;
import com.musicforall.history.handlers.events.TrackLikedEvent;
import com.musicforall.history.handlers.events.TrackListenedEvent;
import com.musicforall.history.model.History;
import com.musicforall.history.util.ServicesTestConfig;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author IliaNik on 29.07.2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
})
@ActiveProfiles("dev")
public class HistoryTest {

    private static final Integer USER_ID = 1111;
    private static final Integer TRACK_ID = 2222;
    private static final Integer TRACK2_ID = 2223;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private HistoryEventListener historyEventListener;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenUsingTheSpyAnnotation_thenObjectIsSpied() {
        final TrackListenedEvent event = new TrackListenedEvent(TRACK_ID, USER_ID);

        historyEventListener.handleTrackListened(event);

        final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(History.class)
                .add(Property.forName("userId").eq(USER_ID))
                .add(Property.forName("eventType").eq(EventType.TRACK_LISTENED))
                .add(Property.forName("trackId").eq(TRACK_ID));
        final History history = historyService.getBy(detachedCriteria);
        assertNotNull(history);
    }

    @Test
    public void testGetLikeCount() {
        final TrackLikedEvent event = new TrackLikedEvent(TRACK_ID, USER_ID);
        historyEventListener.handleTrackLiked(event);

        long numLikes = historyService.getLikeCount(TRACK_ID);
        assertEquals(1, numLikes);

        /* Try to get the like count for non-existing track. */
        numLikes = historyService.getLikeCount(TRACK_ID + 1234);
        assertEquals(0, numLikes);
    }

    @Test
    public void testGetTheMostPopularTrackId(){
        final TrackListenedEvent event = new TrackListenedEvent(TRACK_ID, USER_ID);

        final TrackListenedEvent event2 = new TrackListenedEvent(TRACK2_ID, USER_ID);
        historyEventListener.handleTrackListened(event2);

        historyEventListener.handleTrackListened(event);
        historyEventListener.handleTrackListened(event);

        assertTrue(Objects.equals(historyService.getTheMostPopularTracks(10).get(0), TRACK_ID));
        assertEquals(historyService.getTheMostPopularTracks(20).size(), 2);

        assertEquals(historyService.getTheMostPopularTracks(1).size(), 1); //test limit
    }
}
