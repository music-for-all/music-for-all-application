package com.musicforall.persistence;

import com.musicforall.model.Track;
import com.musicforall.services.track.TrackService;
import com.musicforall.util.ServicesTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.junit.Assert.*;

/**
 * @author ENikolskiy.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class
})
@ActiveProfiles("dev")
public class CurrentStreamsCacheTest {

    @Autowired
    private KeyValueRepository<Integer, Track> cache;

    @Autowired
    private TrackService trackService;

    @Test
    public void testCache() throws Exception {
        final Track track1 = trackService.save(new Track("Valera", "/disk"));
        cache.put(1, track1);
        final Track trackFromCache1 = cache.get(1);

        assertEquals(track1, trackFromCache1);

        final Track track2 = trackService.save(new Track("Valera123", "/disk"));
        cache.put(1, track2);
        final Track trackFromCache2 = cache.get(1);

        assertEquals(track2, trackFromCache2);
        assertNotEquals(trackFromCache1, trackFromCache2);
    }

    @Test
    public void testRemove() throws Exception {
        final Track track1 = trackService.save(new Track("Valera", "/disk"));
        cache.put(1, track1);

        final Track trackFromCache1 = cache.get(1);
        assertNotNull(trackFromCache1);

        cache.remove(1);
        final Track trackFromCache2 = cache.get(1);
        assertNull(trackFromCache2);
    }
}