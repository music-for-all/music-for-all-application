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

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Pukho on 28.06.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
public class TrackServiceTest {

    @Autowired
    TrackService trackService;

    @Autowired
    TagService tagService;

    @Test
    public void testSaveTrackWithoutTags(){
        Track track = new Track("into the wild", "path1");
        trackService.save(track);
        assertNotNull(trackService.get(track.getId()));
    }

    @Test
    public void testSaveTrackWithTags(){
        Set<Tag> tags = new HashSet<>(Arrays.asList(new Tag("rock"), new Tag("alternative")));
        Track track = new Track(tags, "Maybe", "path2");

        trackService.save(track);
        assertNotNull(trackService.get(track.getId()));
    }

    @Test
    public void testSaveSetTracks(){
        Track track1 = new Track("track1", "loc1");
        Track track2 = new Track("track2", "loc2");

        trackService.save(new HashSet<>(Arrays.asList(track1, track2)));
        assertNotNull(trackService.get(track1.getId()));
        assertNotNull(trackService.get(track2.getId()));
    }

    @Test
    public void testAddTagsToSong(){
        Track track1 = new Track("track_for_tags", "loc1");
        trackService.save(track1);
        Set<Tag> tagsForTrack = new HashSet<Tag>(Arrays.asList(new Tag("rock"),
                                                                new Tag("pop")));
        trackService.addTags(track1.getId(), tagsForTrack);

        List<Tag> allTags = tagService.getAllTags();

        assertTrue(allTags.containsAll(tagsForTrack));
        assertFalse(allTags.contains(tagService.get("soul_not_exist")));
    }
}
