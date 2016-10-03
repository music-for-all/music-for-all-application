package com.musicforall.services.history;

import com.musicforall.history.handlers.events.EventType;
import com.musicforall.history.model.History;
import com.musicforall.history.service.history.HistoryService;
import com.musicforall.model.Tag;
import com.musicforall.model.Track;
import com.musicforall.services.track.TrackService;
import com.musicforall.util.ServicesTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.*;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Andrey on 9/5/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@ActiveProfiles("dev")
public class PopularHistoryServiceTest {

    public static final String ALTERNATIVE = "alternative";

    @Autowired
    private TrackService trackService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private PopularHistoryService popularHistoryService;

    @Test
    public void testGetTheMostPopularTags() {
        final Set<Tag> tags = new HashSet<>(Arrays.asList(new Tag("ROOK"), new Tag(ALTERNATIVE)));
        final Track track = new Track("track1", "path2track1", tags);

        trackService.save(track);
        final Set<Tag> tags2 = new HashSet<>(Arrays.asList(new Tag("POP"), new Tag(ALTERNATIVE)));
        final Track track2 = new Track("track2", "path2track2", tags2);
        trackService.save(track2);

        assertEquals(0, popularHistoryService.getPopularTags().size());

        History history = new History(track.getId(), 1, new Date(), 1, EventType.TRACK_LISTENED);
        historyService.record(history);
        history = new History(track2.getId(), 1, new Date(), 1, EventType.TRACK_LISTENED);
        historyService.record(history);

        final Collection<Tag> tags_result = popularHistoryService.getPopularTags();
        assertEquals(3, tags_result.size());
    }
}
