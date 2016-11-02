package com.musicforall.services;

import com.musicforall.model.*;
import com.musicforall.model.user.User;
import com.musicforall.model.user.UserData;
import com.musicforall.services.artist.ArtistService;
import com.musicforall.services.track.TrackService;
import com.musicforall.services.user.UserService;
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

import java.util.*;

import static com.musicforall.services.SearchCriteriaFactory.*;
import static java.util.Collections.singletonList;
import static junit.framework.TestCase.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@ActiveProfiles("dev")
public class SearchCriteriaFactoryTest {

    public static final String USER = "user";
    public static final String PASSWORD = "password";
    public static final String TAG1 = "piano";
    public static final String TAG2 = "cello";

    @Autowired
    private TrackService trackService;
    @Autowired
    private ArtistService artistService;
    @Autowired
    private UserService userService;

    @Test
    public void testBuildTrackSearchCriteria() {
        final Set<Tag> tags = new HashSet<>(Arrays.asList(new Tag(TAG1), new Tag(TAG2)));

        List<Track> tracks = Arrays.asList(
                new Track("Sinfonia in G minor,Op.6,No.6", new Artist("Mozart"), "album1", "/root/track1.mp3", null),
                new Track("Trio Sonata Op.2No.6 in Cmajor", new Artist("Bach"), "album2", "/root/track2.mp3", tags),
                new Track("Moonlight Sonata", new Artist("Beethoven"), "album3", "/root/track3.mp3", null)
        );
        trackService.saveAll(tracks);

        tracks = trackService.getAllLike(
                new SearchTrackRequest("S", null, null, null));
        assertEquals(3, tracks.size());

        tracks = trackService.getAllLike(
                new SearchTrackRequest("Sonata", new Artist(""), null, null));
        assertEquals(2, tracks.size());

        tracks = trackService.getAllLike(
                new SearchTrackRequest("", new Artist("Beethoven"), null, null));
        assertEquals(1, tracks.size());

        tracks = trackService.getAllLike(
                new SearchTrackRequest("Moonlight Sonata", new Artist("Beethoven"), "3", null));
        assertEquals(1, tracks.size());

        tracks = trackService.getAllLike(
                new SearchTrackRequest("minor", new Artist("Mozart"), "album", null));
        assertEquals(1, tracks.size());

        tracks = trackService.getAllLike(
                new SearchTrackRequest("No_title", new Artist("Scarlatti"), "album", singletonList(TAG1)));
        assertEquals(0, tracks.size());

        tracks = trackService.getAllLike(
                new SearchTrackRequest(null, null, null, singletonList(TAG1)));
        assertEquals(1, tracks.size());

        tracks = trackService.getAllLike(
                new SearchTrackRequest("Sonata", new Artist("Bach"), "album2", Arrays.asList(TAG1, TAG2)));
        assertEquals(1, tracks.size());
    }

    @Test
    public void testBuildArtistSearchCriteria() {
        List<Artist> artists;

        artists = artistService.getAllLike(new SearchArtistRequest("Artist_none", singletonList(TAG1)));
        assertEquals(0, artists.size());

        artistService.save(new Artist("Clementi", new HashSet<Tag>(Arrays.asList(new Tag(TAG1), new Tag(TAG2)))));

        artists = artistService.getAllLike(new SearchArtistRequest("cle", singletonList(TAG1)));
        assertEquals(1, artists.size());

        List<String> list = new ArrayList<>(Arrays.asList(TAG1, TAG2));
        artists = artistService.getAllLike(new SearchArtistRequest("menti", list));
        assertEquals(1, artists.size());
    }

    @Test
    public void testBuildUserSearchCriteria() {
        List<UserData> userDatas;
        userDatas = userService.getAllUserDataLike(new SearchUserRequest("qwerty"));
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