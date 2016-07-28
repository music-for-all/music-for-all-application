package com.musicforall.services;

import com.musicforall.common.dao.Dao;
import com.musicforall.model.Tag;
import com.musicforall.model.Track;
import com.musicforall.model.TrackSearchCriteria;
import com.musicforall.services.track.TrackService;
import com.musicforall.util.ServicesTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class })
@ActiveProfiles("dev")
@Transactional
public class SearchCriteriaFactoryTest {

    private Dao dao;

    @Autowired
    private TrackService trackService;

    @Autowired
    public void setDao(Dao dao) {
        this.dao = dao;
    }

    @Test
    public void testBuildTrackSearchCriteria() {
        final Set<Tag> tags = new HashSet<Tag>(Arrays.asList(new Tag("tag1"), new Tag("tag2")));

        List<Track> tracks = Arrays.asList(
                new Track("track", "title1", "artist1", "album1", "/root/track1.mp3", null),
                new Track("track", "title2", "artist2", "album2", "/root/track2.mp3", tags),
                new Track("track", "title3", "artist3", "album3", "/root/track3.mp3", null)
        );
        trackService.saveAll(tracks);

        tracks = dao.getAllBy(SearchCriteriaFactory.createCriteriaFrom(
                new TrackSearchCriteria("title", null, null, null)));
        assertEquals(3, tracks.size());

        tracks = dao.getAllBy(SearchCriteriaFactory.createCriteriaFrom(
                new TrackSearchCriteria("title2", "", null, null)));
        assertEquals(1, tracks.size());

        tracks = dao.getAllBy(SearchCriteriaFactory.createCriteriaFrom(
                new TrackSearchCriteria("title3", "artist", null, null)));
        assertEquals(1, tracks.size());

        tracks = dao.getAllBy(SearchCriteriaFactory.createCriteriaFrom(
                new TrackSearchCriteria("title3", "artist3", "", null)));
        assertEquals(1, tracks.size());

        tracks = dao.getAllBy(SearchCriteriaFactory.createCriteriaFrom(
                new TrackSearchCriteria("title1", "artist1", "album", null)));
        assertEquals(1, tracks.size());

        tracks = dao.getAllBy(SearchCriteriaFactory.createCriteriaFrom(
                new TrackSearchCriteria("No_title", "artist", "album", Arrays.asList("tag1"))));
        assertEquals(0, tracks.size());
    }
}