package com.musicforall.services.track;

import com.musicforall.model.Tag;
import com.musicforall.model.Track;
import com.musicforall.services.tag.TagService;
import com.musicforall.util.ServicesTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Pukho on 28.06.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
public class TrackServiceTest {

    public static final String ROCK = "rock";

    public static final String LOC_1 = "loc1";

    @Autowired
    private TrackService trackService;

    @Autowired
    private TagService tagService;

    @Test
    public void testSaveTrackWithoutTags() {
        final Track track = new Track("into the wild", "path1");
        trackService.save(track);
        assertNotNull(trackService.get(track.getId()));
    }

    @Test
    public void testSaveTrackWithTags() {
        final Set<Tag> tags = new HashSet<>(Arrays.asList(new Tag(ROCK), new Tag("alternative")));
        final Track track = new Track(tags, "Maybe", "path2");

        trackService.save(track);
        assertNotNull(trackService.get(track.getId()));
    }

    @Test
    public void testGetTracks() {
        final Track track = trackService.save(new Track("track_for_test_get", "location"));
        final Track expectedTrack = trackService.get(track.getId());

        assertNotNull(expectedTrack);
        assertEquals(track.getName(), expectedTrack.getName());
        assertEquals(track.getLocation(), expectedTrack.getLocation());
    }

    @Test
    public void testSaveSetTracks() {
        final Track track1 = new Track("track1", LOC_1);
        final Track track2 = new Track("track2", "loc2");

        trackService.saveAll(new HashSet<>(Arrays.asList(track1, track2)));
        assertNotNull(trackService.get(track1.getId()));
        assertNotNull(trackService.get(track2.getId()));
    }

    @Test
    public void testDeleteTrack() {
        final Track track = trackService.save(new Track("track_for_delete", "l1"));
        trackService.delete(track.getId());

        assertNull(trackService.get(track.getId()));
    }

    @Test
    public void testAddTagsToSong() {
        final Track track1 = new Track("track_for_tags", LOC_1);
        trackService.save(track1);
        final Set<Tag> tagsForTrack = new HashSet<Tag>(Arrays.asList(new Tag(ROCK),
                new Tag("pop")));
        trackService.addTags(track1.getId(), tagsForTrack);

        final List<Tag> allTags = tagService.getAllTags();

        assertTrue(allTags.containsAll(tagsForTrack));
        assertFalse(allTags.contains(tagService.get("soul_not_exist")));
    }
}
