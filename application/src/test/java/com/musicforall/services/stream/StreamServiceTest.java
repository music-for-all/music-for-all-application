package com.musicforall.services.stream;

import com.musicforall.model.Track;
import com.musicforall.model.user.User;
import com.musicforall.model.user.UserSettings;
import com.musicforall.services.track.TrackService;
import com.musicforall.services.user.UserService;
import com.musicforall.util.ServicesTestConfig;
import com.musicforall.web.stream.RadioService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.Map;

import static com.musicforall.common.Constants.PASSWORD;
import static com.musicforall.common.Constants.USER;
import static java.util.Collections.singleton;
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
public class StreamServiceTest {

    private static final Integer STUB_USER_ID = 1;
    @Autowired
    @InjectMocks
    private StreamService streamService;
    @Mock
    private RadioService radioService;
    @Autowired
    private TrackService trackService;
    @Autowired
    private UserService userService;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStart() throws Exception {
        final User user = userService.save(
                new User(USER, PASSWORD, "testStart@test.com", new UserSettings(true, "/link")));
        final Track track = trackService.save(new Track("Valera", "/disk"));

        streamService.start(user.getId(), track.getId());
        final Map<Integer, Track> groupedStreams = streamService.getGroupedPublicStreams(singleton(user.getId()));
        assertEquals(track.getId(), groupedStreams.get(user.getId()).getId());
    }

    @Test
    public void testStop() throws Exception {
        final User user = userService.save(
                new User(USER, PASSWORD, "testStop@test.com", new UserSettings(true, "/link")));
        final Track track = trackService.save(new Track("Valera", "/disk"));

        streamService.start(user.getId(), track.getId());
        streamService.stop(user.getId());

        final Map<Integer, Track> groupedStreams = streamService.getGroupedPublicStreams(singleton(user.getId()));
        assertNull(groupedStreams.get(user.getId()));
    }

    @Test
    public void testPublish() throws Exception {
        final User user = userService.save(
                new User(USER, PASSWORD, "testPublish@test.com", new UserSettings(false, "/link")));
        final Track track = trackService.save(new Track("Valera", "/disk"));

        streamService.start(user.getId(), track.getId());

        Map<Integer, Track> groupedStreams = streamService.getGroupedPublicStreams(singleton(user.getId()));
        assertNull(groupedStreams.get(user.getId()));

        streamService.publish(user.getId(), true);

        groupedStreams = streamService.getGroupedPublicStreams(singleton(user.getId()));
        assertNotNull(groupedStreams.get(user.getId()));
    }
}