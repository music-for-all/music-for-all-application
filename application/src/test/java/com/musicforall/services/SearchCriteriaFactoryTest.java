package com.musicforall.services;

import com.musicforall.common.dao.Dao;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.musicforall.services.SearchCriteriaFactory.*;
import static java.util.Collections.singletonList;
import static junit.framework.TestCase.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@ActiveProfiles("dev")
@Transactional
public class SearchCriteriaFactoryTest {

    public static final String USER = "user";
    public static final String PASSWORD = "password";
    private Dao dao;
    @Autowired
    private TrackService trackService;
    @Autowired
    private ArtistService artistService;
    @Autowired
    private UserService userService;

    @Autowired
    public void setDao(Dao dao) {
        this.dao = dao;
    }

    @Before
    public void initialize() {
        final Set<Tag> tags = new HashSet<>(Arrays.asList(new Tag("tag1"), new Tag("tag2")));

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

        tracks = dao.getAllBy(createTrackSearchCriteria(
                new SearchTrackRequest("testTitle", null, null, null)));
        assertEquals(3, tracks.size());

        tracks = dao.getAllBy(createTrackSearchCriteria(
                new SearchTrackRequest("testTitle2", new Artist(""), null, null)));
        assertEquals(1, tracks.size());

        tracks = dao.getAllBy(createTrackSearchCriteria(
                new SearchTrackRequest("testTitle3", new Artist("artist"), null, null)));
        assertEquals(1, tracks.size());

        tracks = dao.getAllBy(createTrackSearchCriteria(
                new SearchTrackRequest("testTitle3", new Artist("artist3"), "", null)));
        assertEquals(1, tracks.size());

        tracks = dao.getAllBy(createTrackSearchCriteria(
                new SearchTrackRequest("testTitle1", new Artist("artist1"), "album", null)));
        assertEquals(1, tracks.size());

        tracks = dao.getAllBy(createTrackSearchCriteria(
                new SearchTrackRequest("No_title", new Artist("artist"), "album", singletonList("tag1"))));
        assertEquals(0, tracks.size());
    }

    @Test
    public void testBuildArtistSearchCriteria() {
        List<Artist> artists;

        artists = dao.getAllBy(createArtistSearchCriteria(
                new SearchArtistRequest("Art", singletonList("tag4"))));
        assertEquals(0, artists.size());

        artistService.save(new Artist("artist4", new HashSet<Tag>(Arrays.asList(new Tag("tag1"), new Tag("tag2")))));
        artists = dao.getAllBy(createArtistSearchCriteria(
                new SearchArtistRequest("Art", singletonList("tag1"))));
        assertEquals(1, artists.size());
    }

    @Test
    public void testBuildUserSearchCriteria() {
        List<User> users;
        users = dao.getAllBy(createUserSearchCriteria(
                new SearchUserRequest("qwe")));
        assertEquals(0, users.size());

        users = dao.getAllBy(createUserSearchCriteria(
                new SearchUserRequest("")));
        assertEquals(0, users.size());

        final UserData userData = new UserData(USER, "firstName", "lastName", "link", "bio", true);
        User user = new User(PASSWORD, "testGetUserLike@test.com", userData);

        userService.save(user);

        SearchUserRequest testEmailRequest = new SearchUserRequest();
        testEmailRequest.setEmail("testGetUse");
        users = dao.getAllBy(createUserSearchCriteria(testEmailRequest));
        assertEquals(1, users.size());

        users = dao.getAllBy(createUserSearchCriteria(new SearchUserRequest("oop")));
        assertEquals(0, users.size());

        SearchUserRequest testFNameRequest = new SearchUserRequest();
        testFNameRequest.setFirstName("first");
        users = dao.getAllBy(createUserSearchCriteria(testFNameRequest));
        assertEquals(1, users.size());

        users = dao.getAllBy(createUserSearchCriteria(new SearchUserRequest("use")));
        assertEquals(1, users.size());

        SearchUserRequest testLNameRequest = new SearchUserRequest();
        testLNameRequest.setLastName("last");
        users = dao.getAllBy(createUserSearchCriteria(testLNameRequest));
        assertEquals(1, users.size());
    }

    @Test
    public void testNullSearchCriteria() throws NoSuchMethodException {
        assertEquals(null, createArtistSearchCriteria(null));
        assertEquals(null, createTrackSearchCriteria(null));
        assertEquals(null, createUserSearchCriteria(null));
    }
}