package com.musicforall.history.service.history;

import com.musicforall.history.handlers.events.EventType;
import com.musicforall.history.model.History;
import com.musicforall.history.service.DBHistoryPopulateService;
import com.musicforall.history.util.ServicesTestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
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
public class HistoryServiceImplTest {

    private static final Integer USER_ID = 1111;
    private static final Integer TRACK_ID = 2222;
    private static final Integer TOP_TRACK_ID = 2225;

    @Autowired
    private HistoryService service;

    @Autowired
    private DBHistoryPopulateService populateService;

    @Before
    public void setUp() {
        populateService.populateTrackListened(Arrays.asList(TRACK_ID, 2, 3, 4, 5), USER_ID);
    }

    @Test
    public void testRecord() throws Exception {
        SearchHistoryParams params = SearchHistoryParams.create()
                .eventType(EventType.TRACK_LISTENED)
                .userId(USER_ID).trackId(TRACK_ID).get();
        int initialSize = service.getAllBy(params).size();
        service.record(new History(TRACK_ID, new Date(), USER_ID, EventType.TRACK_LISTENED));
        int currentSize = service.getAllBy(params).size();
        assertEquals(currentSize - initialSize, 1);
    }

    @Test
    public void testGetTheMostPopularTracks() throws Exception {
        IntStream.range(0, 100)
                .mapToObj(i -> new History(TOP_TRACK_ID, new Date(), USER_ID, EventType.TRACK_LISTENED))
                .forEach(service::record);
        Collection<History> histories = service.getAllBy(SearchHistoryParams.create().eventType(EventType.TRACK_LISTENED).get());
        Map<Integer, Integer> listenedByTrackId = histories.stream()
                .collect(Collectors.toMap(History::getTrackId, h -> 1, Integer::sum));
        List<Integer> topListened = listenedByTrackId.keySet().stream()
                .sorted((i1, i2) -> listenedByTrackId.get(i2).compareTo(listenedByTrackId.get(i1)))
                .limit(10).collect(Collectors.toList());
        //compare only first top listened track
        //because number of listenings for other tracks is generated randomly
        assertEquals(service.getTheMostPopularTracks().get(0), TOP_TRACK_ID);
        assertEquals(service.getTheMostPopularTracks().size(), topListened.size());
    }

    @Test
    public void testGetAllBy() throws Exception {
        Collection<History> histories = service.getAllBy(SearchHistoryParams.create().eventType(EventType.TRACK_LIKED).get());
        assertTrue(histories.stream().allMatch(h -> h.getEventType() == EventType.TRACK_LIKED));
        histories = service.getAllBy(SearchHistoryParams.create().eventType(EventType.TRACK_LISTENED).userId(USER_ID).get());
        assertTrue(histories.stream().allMatch(h -> h.getEventType() == EventType.TRACK_LISTENED && h.getUserId().equals(USER_ID)));
        histories = service.getAllBy(SearchHistoryParams.create().trackId(TRACK_ID).get());
        assertTrue(histories.stream().allMatch(h -> h.getTrackId().equals(TRACK_ID)));
    }

    @Test
    public void testGetLikeCount() throws Exception {
        long numLikes = service.getLikeCount(TRACK_ID);
        assertTrue(numLikes > 0);

        /* Try to get the like count for non-existing track. */
        numLikes = service.getLikeCount(TRACK_ID + 1234);
        assertEquals(0, numLikes);
    }
}
