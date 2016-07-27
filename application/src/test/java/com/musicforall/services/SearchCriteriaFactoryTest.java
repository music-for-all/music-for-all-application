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

import java.util.*;

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

        List<Track> tracks = Arrays.asList(
                new Track("track", "title1", "artist1", "album1", "/root/track1.mp3", null),
                new Track("track", "title2", "artist2", "album2", "/root/track2.mp3", tags),
                new Track("track", "title3", "artist3", "album3", "/root/track3.mp3", null)
        );
        trackService.saveAll(tracks);

        tracks = dao.getAllBy(SearchCriteriaFactory.buildTrackSearchCriteria(
                new SearchCriteria("title", null, null, null)));
        assertEquals(3, tracks.size());

        tracks = dao.getAllBy(SearchCriteriaFactory.buildTrackSearchCriteria(
                new SearchCriteria("title2", "", null, null)));
        assertEquals(1, tracks.size());

        tracks = dao.getAllBy(SearchCriteriaFactory.buildTrackSearchCriteria(
                new SearchCriteria("title3", "artist", null, null)));
        assertEquals(1, tracks.size());

        tracks = dao.getAllBy(SearchCriteriaFactory.buildTrackSearchCriteria(
                new SearchCriteria("title3", "artist3", "", null)));
        assertEquals(1, tracks.size());

        tracks = dao.getAllBy(SearchCriteriaFactory.buildTrackSearchCriteria(
                new SearchCriteria("title1", "artist1", "album", null)));
        assertEquals(1, tracks.size());

        dao.get(Track.class, 2);

//        tracks = dao.getAllBy(SearchCriteriaFactory.buildTrackSearchCriteria(
//                new SearchCriteria("title", "artist2", "album2", Arrays.asList("tag2"))));

        /* Convert tags collection into a comma-separated values. */
        StringBuilder tagsString = new StringBuilder();

        for (Iterator<Tag> tagIterator = tags.iterator(); tagIterator.hasNext();) {

            tagsString.append(String.format("'%s'", tagIterator.next().getName()));
            if (tagIterator.hasNext()) {
                tagsString.append(',');
            }
        }
        System.err.printf("%s\n", tagsString);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("title", "title2");
        parameters.put("artist", "artist2");
        parameters.put("album", "album2");
        parameters.put("tags", tagsString.toString());
        parameters.put("tagCount", String.valueOf(tags.size()));

        tracks = dao.getAllBy(
                "select track from Track track " +
                        "join track.tags tag " +
                        "where tag.name in (:tags) " +
                        "and track.id in (" +
                        "select trackTmp.id " +
                        "from Track trackTmp " +
                        "join trackTmp.tags tagTmp " +
                        "group by trackTmp " +
                        "having count(tagTmp)=:tagCount) " +
                        "group by track " +
                        "having count(tag)=:tagCount",
                parameters);

        assertEquals(1, tracks.size());

        tracks = dao.getAllBy(SearchCriteriaFactory.buildTrackSearchCriteria(
                new SearchCriteria("No_title", "artist", "album", Arrays.asList("tag"))));
        assertEquals(0, tracks.size());
    }
}