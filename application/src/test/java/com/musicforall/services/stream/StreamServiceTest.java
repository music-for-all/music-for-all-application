package com.musicforall.services.stream;

import com.musicforall.model.Track;
import com.musicforall.model.user.User;
import com.musicforall.model.user.UserData;
import com.musicforall.services.track.TrackService;
import com.musicforall.services.user.UserService;
import com.musicforall.util.ServicesTestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

    @Autowired
    private StreamService streamService;
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
        final User user = new User(PASSWORD, "testStart@test.com");
        final UserData userData = new UserData(user, USER);
        userData.setPublicRadio(true);
        user.setUserData(userData);
        userService.save(user);
        final Track track = trackService.save(new Track("Valera", "/disk"));

        streamService.start(user.getId(), track.getId());
        final Map<Integer, Track> groupedStreams = streamService.getGroupedPublicStreams(singleton(user.getId()));
        assertEquals(track.getId(), groupedStreams.get(user.getId()).getId());
    }

    @Test
    public void testStop() throws Exception {
        final User user = new User(PASSWORD, "testStop@test.com");
        final UserData userData = new UserData(user, USER);
        userData.setPublicRadio(true);
        user.setUserData(userData);
        userService.save(user);
        final Track track = trackService.save(new Track("Valera", "/disk"));

        streamService.start(user.getId(), track.getId());
        streamService.stop(user.getId());

        final Map<Integer, Track> groupedStreams = streamService.getGroupedPublicStreams(singleton(user.getId()));
        assertNull(groupedStreams.get(user.getId()));
    }

    @Test
    public void testPublish() throws Exception {
        final User user = new User(PASSWORD, "testPublish@test.com");
        final UserData userData = new UserData(user, USER);
        userData.setPublicRadio(false);
        user.setUserData(userData);
        userService.save(user);
        final Track track = trackService.save(new Track("Valera", "/disk"));

        streamService.start(user.getId(), track.getId());

        Map<Integer, Track> groupedStreams = streamService.getGroupedPublicStreams(singleton(user.getId()));
        assertNull(groupedStreams.get(user.getId()));

        streamService.switchRadio(user.getId());

        groupedStreams = streamService.getGroupedPublicStreams(singleton(user.getId()));
        assertNotNull(groupedStreams.get(user.getId()));
    }
}