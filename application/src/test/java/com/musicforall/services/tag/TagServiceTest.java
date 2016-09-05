package com.musicforall.services.tag;

import com.musicforall.history.handlers.events.EventType;
import com.musicforall.history.model.History;
import com.musicforall.history.service.history.HistoryService;
import com.musicforall.model.Tag;
import com.musicforall.model.Track;
import com.musicforall.services.recommendation.RecommendationService;
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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Pukho on 22.06.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@ActiveProfiles("dev")
public class TagServiceTest {

    public static final String ALTERNATIVE = "alternative";

    @Autowired
    private TagService tagService;

    @Autowired
    private TrackService trackService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RecommendationService recommendationService;

    @Test
    public void testSaveAllTagsGetAllTags() {
        final Tag tag1 = new Tag("tag_for_save1");
        final Tag tag2 = new Tag("tag_for_save2");

        final Collection<Tag> savesTags = tagService.saveAll(Arrays.asList(tag1, tag2));
        assertTrue(tagService.getAllTags().containsAll(savesTags));
    }

    @Test
    public void testGetTracks() {
        final String tag_for_test_get = "tag_for_test_get";
        final Tag tag = tagService.save(tag_for_test_get);
        final Tag expectedTag = tagService.get(tag_for_test_get);

        assertEquals(expectedTag, tag);
    }

    @Test
    public void getTagIsNotExist() {
        assertNull(tagService.get("this_tag_isnt_exist"));
    }

    @Test
    public void testGetTagsLike() {
        final Tag tag1 = new Tag("rock");
        final Tag tag2 = new Tag("indirock");
        final Tag tag3 = new Tag("hard-rock");
        final Tag tag4 = new Tag("jazz");
        tagService.saveAll(Arrays.asList(tag1, tag2, tag3, tag4));
        assertEquals(3, tagService.getAllLike("rock").size());
    }

    @Test
    public void testGetTheMostPopularTags() {
        final Set<Tag> tags = new HashSet<>(Arrays.asList(new Tag("ROOK"), new Tag(ALTERNATIVE)));
        final Track track = new Track("track1", "path2track1", tags);

        trackService.save(track);
        final Set<Tag> tags2 = new HashSet<>(Arrays.asList(new Tag("POP"), new Tag(ALTERNATIVE)));
        final Track track2 = new Track("track2", "path2track2", tags2);

        trackService.save(track2);
        History history = new History(track.getId(), 1, new Date(), 1, EventType.TRACK_LISTENED);
        historyService.record(history);
        history = new History(track2.getId(), 1, new Date(), 1, EventType.TRACK_LISTENED);
        historyService.record(history);
        final Collection<Tag> tags_result = recommendationService.getPopularTags();
        assertEquals(3, tags_result.size());
    }
}
