package com.musicforall.services;

import com.musicforall.common.dao.Dao;
import com.musicforall.model.Artist;
import com.musicforall.model.SearchTrackRequest;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
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
                new Track("track", "testTitle1", new Artist("artist1"), "album1", "/root/track1.mp3", null),
                new Track("track", "testTitle2", new Artist("artist2"), "album2", "/root/track2.mp3", tags),
                new Track("track", "testTitle3", new Artist("artist3"), "album3", "/root/track3.mp3", null)
        );
        trackService.saveAll(tracks);

        tracks = dao.getAllBy(SearchCriteriaFactory.createTrackSearchCriteria(
                new SearchTrackRequest("testTitle", null, null, null)));
        assertEquals(3, tracks.size());

        tracks = dao.getAllBy(SearchCriteriaFactory.createTrackSearchCriteria(
                new SearchTrackRequest("testTitle2", new Artist(""), null, null)));
        assertEquals(1, tracks.size());

        tracks = dao.getAllBy(SearchCriteriaFactory.createTrackSearchCriteria(
                new SearchTrackRequest("testTitle3", new Artist("artist"), null, null)));
        assertEquals(1, tracks.size());

        tracks = dao.getAllBy(SearchCriteriaFactory.createTrackSearchCriteria(
                new SearchTrackRequest("testTitle3",  new Artist("artist3"), "", null)));
        assertEquals(1, tracks.size());

        tracks = dao.getAllBy(SearchCriteriaFactory.createTrackSearchCriteria(
                new SearchTrackRequest("testTitle1",  new Artist("artist1"), "album", null)));
        assertEquals(1, tracks.size());

        tracks = dao.getAllBy(SearchCriteriaFactory.createTrackSearchCriteria(
                new SearchTrackRequest("No_title",  new Artist("artist"), "album", Arrays.asList("tag1"))));
        assertEquals(0, tracks.size());
    }
}