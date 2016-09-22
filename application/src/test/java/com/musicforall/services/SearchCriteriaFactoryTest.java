package com.musicforall.services;

import com.musicforall.common.dao.Dao;
import com.musicforall.model.*;
import com.musicforall.services.artist.ArtistService;
import com.musicforall.services.track.TrackService;
import com.musicforall.util.ServicesTestConfig;
import org.hibernate.criterion.DetachedCriteria;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    private ArtistService artistService;

    @Autowired
    public void setDao(Dao dao) {
        this.dao = dao;
    }

    @Before
    public void initialize() {
        final Set<Tag> tags = new HashSet<Tag>(Arrays.asList(new Tag("tag1"), new Tag("tag2")));

        List<Track> tracks = Arrays.asList(
                new Track("track", "testTitle1", new Artist("artist1"), "album1", "/root/track1.mp3", null),
                new Track("track", "testTitle2", new Artist("artist2"), "album2", "/root/track2.mp3", tags),
                new Track("track", "testTitle3", new Artist("artist3"), "album3", "/root/track3.mp3", null)
        );
        trackService.saveAll(tracks);
    }

    @Test
    public void testBuildTrackSearchCriteria() {
        List<Track> tracks;

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
                new SearchTrackRequest("testTitle3", new Artist("artist3"), "", null)));
        assertEquals(1, tracks.size());

        tracks = dao.getAllBy(SearchCriteriaFactory.createTrackSearchCriteria(
                new SearchTrackRequest("testTitle1", new Artist("artist1"), "album", null)));
        assertEquals(1, tracks.size());

        tracks = dao.getAllBy(SearchCriteriaFactory.createTrackSearchCriteria(
                new SearchTrackRequest("No_title", new Artist("artist"), "album", Arrays.asList("tag1"))));
        assertEquals(0, tracks.size());
    }

    @Test
    public void testBuildArtistSearchCriteria() {
        List<Artist> artists;

        artists = dao.getAllBy(SearchCriteriaFactory.createArtistSearchCriteria(
                new SearchArtistRequest("Art", Collections.singletonList("tag4"))));
        assertEquals(0, artists.size());

        artistService.save(new Artist("artist4", new HashSet<Tag>(Arrays.asList(new Tag("tag1"), new Tag("tag2")))));
        artists = dao.getAllBy(SearchCriteriaFactory.createArtistSearchCriteria(
                new SearchArtistRequest("Art", Collections.singletonList("tag1"))));
        assertEquals(1, artists.size());
    }

    @Test
    public void testNullSearchCriteria() throws NoSuchMethodException {

        DetachedCriteria ArtistSearchFactory = SearchCriteriaFactory.createArtistSearchCriteria(null);
        assertEquals(null, ArtistSearchFactory);

        DetachedCriteria TrackSearchFactory = SearchCriteriaFactory.createTrackSearchCriteria(null);
        assertEquals(null, TrackSearchFactory);
    }
}