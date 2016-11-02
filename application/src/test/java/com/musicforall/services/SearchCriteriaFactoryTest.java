package com.musicforall.services;

import com.musicforall.model.*;
import com.musicforall.model.user.User;
import com.musicforall.model.user.UserData;
import com.musicforall.services.artist.ArtistService;
import com.musicforall.services.track.TrackService;
import com.musicforall.services.user.UserService;
import com.musicforall.util.ServicesTestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.*;

import static com.musicforall.services.SearchCriteriaFactory.*;
import static java.util.Collections.singletonList;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@ActiveProfiles("dev")
public class SearchCriteriaFactoryTest {

    public static final String USER = "user";
    public static final String PASSWORD = "password";
    public static final String TAG1 = "tag1";
    public static final String TAG2 = "tag2";

    @Autowired
    private TrackService trackService;
    @Autowired
    private ArtistService artistService;
    @Autowired
    private UserService userService;

    @Before
    public void initialize() {
        final Set<Tag> tags = new HashSet<>(Arrays.asList(new Tag(TAG1), new Tag(TAG2)));

        List<Track> tracks = Arrays.asList(
                new Track("testTitle1", new Artist("artist1"), "album1", "/root/track1.mp3", null),
                new Track("testTitle2", new Artist("artist2"), "album2", "/root/track2.mp3", tags),
                new Track("testTitle3", new Artist("artist3"), "album3", "/root/track3.mp3", null)
        );
        trackService.saveAll(tracks);
    }

    @Test
    public void testBuildTrackSearchCriteria() {
        List<Track> tracks;

        tracks = trackService.getAllLike(
                new SearchTrackRequest("testTitle", null, null, null));
        assertTrue(tracks.size() >= 1);

        tracks = trackService.getAllLike(
                new SearchTrackRequest("testTitle2", new Artist(""), null, null));
        assertTrue(tracks.size() >= 1);

        tracks = trackService.getAllLike(
                new SearchTrackRequest("testTitle3", new Artist("artist"), null, null));
        assertTrue(tracks.size() >= 1);

        tracks = trackService.getAllLike(
                new SearchTrackRequest("testTitle3", new Artist("artist3"), "", null));
        assertTrue(tracks.size() >= 1);

        tracks = trackService.getAllLike(
                new SearchTrackRequest("testTitle1", new Artist("artist1"), "album", null));
        assertTrue(tracks.size() >= 1);

        tracks = trackService.getAllLike(
                new SearchTrackRequest("No_title", new Artist("artist"), "album", singletonList(TAG1)));
        assertEquals(0, tracks.size());

        tracks = trackService.getAllLike(
                new SearchTrackRequest(null, null, null, singletonList(TAG1)));
        assertTrue(tracks.size() >= 1);

        tracks = trackService.getAllLike(
                new SearchTrackRequest("testTitle2", new Artist("artist2"), "album2", Arrays.asList(TAG1, TAG2)));
        assertTrue(tracks.size() >= 1);
    }

    @Test
    public void testBuildArtistSearchCriteria() {
        List<Artist> artists;

        artists = artistService.getAllLike(new SearchArtistRequest("Art_none", singletonList(TAG1)));
        assertEquals(0, artists.size());

        artistService.save(new Artist("artist4", new HashSet<Tag>(Arrays.asList(new Tag(TAG1), new Tag(TAG2)))));

        artists = artistService.getAllLike(new SearchArtistRequest("Art", singletonList(TAG1)));
        assertEquals(1, artists.size());

        List<String> list = new ArrayList<>(Arrays.asList(TAG1, TAG2));
        artists = artistService.getAllLike(new SearchArtistRequest("art", list));
        assertEquals(1, artists.size());
    }

    @Test
    public void testBuildUserSearchCriteria() {
        List<UserData> userDatas;
        userDatas = userService.getAllUserDataLike(new SearchUserRequest("qwe"));
        assertEquals(0, userDatas.size());

        userDatas = userService.getAllUserDataLike(new SearchUserRequest(""));
        assertEquals(0, userDatas.size());

        User user = new User(PASSWORD, "testGetUserLike@test.com");
        user.setUserData(new UserData(user, USER, "firstName", "lastName", "link", "bio", true));

        userService.save(user);

        userDatas = userService.getAllUserDataLike(new SearchUserRequest("oop"));
        assertEquals(0, userDatas.size());

        SearchUserRequest testFNameRequest = new SearchUserRequest();
        testFNameRequest.setFirstName("first");
        userDatas = userService.getAllUserDataLike(testFNameRequest);
        assertEquals(1, userDatas.size());

        userDatas = userService.getAllUserDataLike(new SearchUserRequest("use"));
        assertEquals(1, userDatas.size());

        SearchUserRequest testLNameRequest = new SearchUserRequest();
        testLNameRequest.setLastName("last");
        userDatas = userService.getAllUserDataLike(testLNameRequest);
        assertEquals(1, userDatas.size());
    }

    @Test
    public void testNullSearchCriteria() throws NoSuchMethodException {
        assertEquals(null, createArtistSearchCriteria(null));
        assertEquals(null, createTrackSearchCriteria(null));
        assertEquals(null, createUserSearchCriteria(null));
    }
}