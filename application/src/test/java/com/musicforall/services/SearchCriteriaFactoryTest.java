package com.musicforall.services;

import com.musicforall.common.dao.Dao;
import com.musicforall.model.SearchCriteria;
import com.musicforall.model.Tag;
import com.musicforall.model.Track;
import com.musicforall.services.track.TrackService;
import com.musicforall.util.ServicesTestConfig;
import org.hibernate.criterion.DetachedCriteria;
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
        final Set<Tag> tags = new HashSet<Tag>(Arrays.asList(new Tag("tag"), new Tag("tag2")));

        trackService.save(new Track("track", "title", "artist", "album", "/root/track.mp3", tags));

        List<Track> tracks = dao.getAllBy(SearchCriteriaFactory.buildTrackSearchCriteria(
                new SearchCriteria("title", null, null, null)));
        assertEquals(1, tracks.size());

        tracks = dao.getAllBy(SearchCriteriaFactory.buildTrackSearchCriteria(
                new SearchCriteria("title", "", null, null)));
        assertEquals(1, tracks.size());

        tracks = dao.getAllBy(SearchCriteriaFactory.buildTrackSearchCriteria(
                new SearchCriteria("title", "artist", null, null)));
        assertEquals(1, tracks.size());

        tracks = dao.getAllBy(SearchCriteriaFactory.buildTrackSearchCriteria(
                new SearchCriteria("title", "artist", "", null)));
        assertEquals(1, tracks.size());

        tracks = dao.getAllBy(SearchCriteriaFactory.buildTrackSearchCriteria(
                new SearchCriteria("title", "artist", "album", null)));
        assertEquals(1, tracks.size());

        tracks = dao.getAllBy(SearchCriteriaFactory.buildTrackSearchCriteria(
                new SearchCriteria("title", "artist", "album", Arrays.asList("tag"))));
        assertEquals(1, tracks.size());
    }
}
