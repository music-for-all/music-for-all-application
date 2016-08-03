package com.musicforall.services.track;

import com.musicforall.model.*;
import com.musicforall.services.tag.TagService;
import com.musicforall.util.ServicesTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.*;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Pukho on 28.06.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class })
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TrackTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
@ActiveProfiles("dev")
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
        final Track track = new Track("Maybe", "path2", tags);

        trackService.save(track);
        assertNotNull(trackService.get(track.getId()));
    }

    @Test
    public void testGetTracks() {
        final Track track = trackService.save(new Track("track_for_test_get", "location"));
        Track expectedTrack = trackService.get(track.getId());

        assertNotNull(expectedTrack);
        assertEquals(expectedTrack.getName(), track.getName());
        assertEquals(expectedTrack.getLocation(), track.getLocation());

        expectedTrack = trackService.get(1000);

        assertNull(expectedTrack);
    }

    @Test
    public void testSaveTrackSet() {
        final Track track1 = new Track("track1", LOC_1);
        final Track track2 = new Track("track2", "loc2");

        Collection<Track> savedTracks = trackService.saveAll(new HashSet<>(Arrays.asList(track1, track2)));
        assertNotNull(trackService.get(track1.getId()));
        assertNotNull(trackService.get(track2.getId()));
        assertNotNull(savedTracks);
        assertEquals(2, savedTracks.size());
    }

    @Test
    public void testDeleteTrack() {
        final Track track = trackService.save(new Track("track_for_delete", "l1"));
        trackService.delete(track.getId());
        assertNull(trackService.get(track.getId()));

    }

    @Test
    public void testAddTagsToTrack() {
        final Track track1 = new Track("track_for_tags", LOC_1);
        trackService.save(track1);
        final Set<Tag> tagsForTrack = new HashSet<Tag>(Arrays.asList(new Tag(ROCK),
                new Tag("pop")));
        trackService.addTags(track1.getId(), tagsForTrack);

        final List<Tag> allTags = tagService.getAllTags();
        assertNotNull(allTags);
        assertTrue(allTags.size() >= tagsForTrack.size());
        assertTrue(allTags.containsAll(tagsForTrack));
        assertFalse(allTags.contains(tagService.get("soul_not_exist")));
    }

    @Test
    public void testSearchByName() {
        final Track track1 = new Track("sim", LOC_1);
        final Track track2 = new Track("Sim2", LOC_1);
        final Track track3 = new Track("asdfsimiliar", LOC_1);
        final Track track4 = new Track("asdifferent", LOC_1);

        trackService.saveAll(Arrays.asList(track1, track2, track3, track4));
        Collection<Track> tracks = trackService.getAllByName("Sim");
        assertEquals(3, tracks.size());

        tracks = trackService.getAllByName("NO-NAME");
        assertEquals(0, tracks.size());
    }

    @Test
    public void testGetAllLike() {

        final Set<Tag> tags = new HashSet<Tag>(Arrays.asList(new Tag("tag1"), new Tag("tag2")));

        List<Track> tracks = Arrays.asList(
                new Track("track", "title1", "artist1", "album1", "/root/track1.mp3", null),
                new Track("track", "title2", "artist2", "album2", "/root/track2.mp3", tags),
                new Track("track", "title3", "artist3", "album3", "/root/track3.mp3", null)
        );
        trackService.saveAll(tracks);

        SearchCriteria searchCriteria = new SearchCriteria("title", "artist", "album", Arrays.asList("tag1", "tag2"));
        tracks = trackService.getAllLike(searchCriteria);
        assertNotNull(tracks);
        assertEquals(1, tracks.size());

        tracks = trackService.getAllLike(new SearchCriteria());
        assertTrue(tracks.size() >= 3);
    }

    @Test
    public void testFindAll() {
        Track track = new Track("track3", "/track3.mp3");
        List<Track> tracks = trackService.findAll();
        assertNotNull(tracks);
        assertTrue(tracks.size() >= 1);
    }

    @Test
    @WithUserDetails("user")
    public void testLike() {

        Track track = new Track("track2", "/track2.mp3");
        assertNotNull(trackService.save(track));
        Integer id = track.getId();
        assertTrue(trackService.like(id));
        int numLikes = trackService.getLikeCount(id);
        assertEquals(1, numLikes);
        assertFalse(trackService.like(track.getId() + 1000));
    }

    @Test
    @WithUserDetails("user")
    public void testGetLikeCount() {
        Track track = new Track("track1", "/track1.mp3");
        assertNotNull(trackService.save(track));
        Integer id = track.getId();

        int numLikes = trackService.getLikeCount(id);
        assertEquals(0, numLikes);

        assertTrue(trackService.like(id));
        numLikes = trackService.getLikeCount(id);
        assertEquals(1, numLikes);

        numLikes = trackService.getLikeCount(id + 1000);
        assertEquals(0, numLikes);
    }
}
