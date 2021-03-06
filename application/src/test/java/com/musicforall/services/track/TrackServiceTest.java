package com.musicforall.services.track;

import com.musicforall.history.model.History;
import com.musicforall.history.service.DBHistoryPopulateService;
import com.musicforall.history.service.history.SearchHistoryParams;
import com.musicforall.model.*;
import com.musicforall.model.user.User;
import com.musicforall.services.follower.FollowerService;
import com.musicforall.services.playlist.PlaylistService;
import com.musicforall.services.recommendation.RecommendationService;
import com.musicforall.services.tag.TagService;
import com.musicforall.services.user.UserService;
import com.musicforall.util.SecurityUtil;
import com.musicforall.util.ServicesTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.musicforall.history.handlers.events.EventType.TRACK_LISTENED;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.junit.Assert.*;

/**
 * Created by Pukho on 28.06.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TrackTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
@ActiveProfiles("dev")
public class TrackServiceTest {

    public static final String ROCK = "rock";

    public static final String LOC_1 = "loc1";

    @Autowired
    private TrackService trackService;

    @Autowired
    private TagService tagService;

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private FollowerService followerService;

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private DBHistoryPopulateService dbHistoryPopulateService;

    @Autowired
    private UserService userService;

    @Test
    public void testSaveTrackWithoutTags() {
        final Track track = new Track("into the wild", "path1");
        trackService.save(track);
        assertNotNull(trackService.get(track.getId()));
    }

    @Test
    public void testSaveTrackWithTags() {
        final Set<Tag> tags = new HashSet<>(Arrays.asList(new Tag(ROCK), new Tag("alternative")));
        final Track track = new Track("Maybe", "path2", tags);

        trackService.save(track);
        assertNotNull(trackService.get(track.getId()));
    }

    @Test
    public void testGetTracks() {
        final Track track = trackService.save(new Track("track_for_test_get", "location"));
        Track expectedTrack = trackService.get(track.getId());

        assertNotNull(expectedTrack);
        assertEquals(expectedTrack.getName(), track.getName());
        assertEquals(expectedTrack.getLocation(), track.getLocation());

        expectedTrack = trackService.get(1000);

        assertNull(expectedTrack);
    }

    @Test
    public void testSaveTrackSet() {
        final Track track1 = new Track("track1", LOC_1);
        final Track track2 = new Track("track2", "loc2");

        Collection<Track> savedTracks = trackService.saveAll(new HashSet<>(Arrays.asList(track1, track2)));
        assertNotNull(trackService.get(track1.getId()));
        assertNotNull(trackService.get(track2.getId()));
        assertNotNull(savedTracks);
        assertEquals(2, savedTracks.size());
    }

    @Test
    public void testDeleteTrack() {
        final Track track = trackService.save(new Track("track_for_delete", "l1"));
        trackService.delete(track.getId());
        assertNull(trackService.get(track.getId()));

    }

    @Test
    public void testAddTagsToTrack() {
        final Track track1 = new Track("track_for_tags", LOC_1);
        trackService.save(track1);
        final Set<Tag> tagsForTrack = new HashSet<Tag>(Arrays.asList(new Tag(ROCK),
                new Tag("pop")));
        trackService.addTags(track1.getId(), tagsForTrack);

        final List<Tag> allTags = tagService.getAllTags();
        assertNotNull(allTags);
        assertTrue(allTags.size() >= tagsForTrack.size());
        assertTrue(allTags.containsAll(tagsForTrack));
        assertFalse(allTags.contains(tagService.get("soul_not_exist")));
    }

    @Test
    public void testSearchByName() {
        final Track track1 = new Track("sim", LOC_1);
        final Track track2 = new Track("Sim2", LOC_1);
        final Track track3 = new Track("asdfsimiliar", LOC_1);
        final Track track4 = new Track("asdifferent", LOC_1);

        trackService.saveAll(Arrays.asList(track1, track2, track3, track4));
        Collection<Track> tracks = trackService.getAllByName("Sim");
        assertEquals(3, tracks.size());

        tracks = trackService.getAllByName("NO-NAME");
        assertEquals(0, tracks.size());
    }

    @Test
    public void testGetAllLikes() {

        final Set<Tag> tags = new HashSet<>(Arrays.asList(new Tag("tag1"), new Tag("tag2")));

        List<Track> tracks = Arrays.asList(
                new Track("track1", new Artist("artist1"), "album1", "/root/track1.mp3", null),
                new Track("track2", new Artist("artist2"), "album2", "/root/track2.mp3", tags),
                new Track("track3", new Artist("artist3"), "album3", "/root/track3.mp3", null)
        );
        trackService.saveAll(tracks);

        SearchTrackRequest searchCriteria = new SearchTrackRequest("track", new Artist("artist"), "album", Arrays.asList("tag1", "tag2"));
        tracks = trackService.getAllLike(searchCriteria);
        assertNotNull(tracks);
        assertEquals(1, tracks.size());

        tracks = trackService.getAllLike(new SearchTrackRequest());
        assertTrue(tracks.size() >= 3);
    }

    @Test
    public void testGetAllByIds() {
        List<Track> tracks = Arrays.asList(
                new Track("track", new Artist("artist1"), "album1", "/root/track1.mp3", null),
                new Track("track", new Artist("artist2"), "album2", "/root/track2.mp3", null),
                new Track("track", new Artist("artist3"), "album3", "/root/track3.mp3", null)
        );
        Collection<Track> savedTracks = trackService.saveAll(tracks);
        List<Integer> ids = savedTracks.stream().limit(2).map(Track::getId).collect(Collectors.toList());
        Collection<Track> foundTracks = trackService.getAllByIds(ids);
        assertEquals(foundTracks.size(), ids.size());
        assertTrue(foundTracks.stream().allMatch(t -> ids.contains(t.getId())));
    }

    @Test
    public void testGetAllByEmptyIds() {
        final Collection<Track> tracks = trackService.getAllByIds(Collections.emptyList());
        assertTrue(tracks.isEmpty());
    }

    @Test
    public void testGetAllByNullIds() {
        final Collection<Track> tracks = trackService.getAllByIds(null);
        assertTrue(tracks.isEmpty());
    }

    @Test
    public void testFindAll() {
        trackService.save(new Track("track3", "/track3.mp3"));
        List<Track> tracks = trackService.findAll();
        assertNotNull(tracks);
        assertTrue(tracks.size() >= 1);
    }

    @Test
    @WithUserDetails("user@example.com")
    public void testGetRecommendedTracks() {

        final Track track1 = new Track("track", new Artist("artist1"), "album1", "/root/track1.mp3", null);
        final Track track2 = new Track("track", new Artist("artist2"), "album2", "/root/track2.mp3", null);
        final Track track3 = new Track("track", new Artist("artist3"), "album3", "/root/track3.mp3", null);
        final Track track4 = new Track("track", new Artist("artist4"), "album4", "/root/track4.mp3", null);

        trackService.save(track1);
        trackService.save(track2);
        trackService.save(track3);
        trackService.save(track4);
        final List<Integer> trackIds = Arrays.asList(track1.getId(), track2.getId(), track3.getId());

        final User followingUser = new User("228", "ya@gmail.com");
        userService.save(followingUser);

        followerService.follow(SecurityUtil.currentUserId(), followingUser.getId());

        dbHistoryPopulateService.populateTrackLikedByFollowedUsers(trackIds, followingUser.getId());

        Collection<Track> tracks = recommendationService.getFollowingsRecommendedTracks();
        assertNotNull(tracks);
        assertEquals(3, tracks.size());

        final Playlist playlist = playlistService.save("demo");
        final Set<Integer> tracksIds = trackService.findAll().stream()
                .map(Track::getId)
                .collect(Collectors.toSet());

        playlistService.addTracks(playlist.getId(), tracksIds);

        tracks = recommendationService.getFollowingsRecommendedTracks();
        assertEquals(0, tracks.size());

        playlistService.delete(playlist.getId());
    }

    @Test
    @WithUserDetails("user@example.com")
    public void testGetArtistMostPopularTracks() throws Exception {

        final Track track1 = new Track("track1", new Artist("The Beatles"), "album1", "/root/track1.mp3", null);
        final Track track2 = new Track("track2", new Artist("The Beatles"), "album2", "/root/track2.mp3", null);
        final Track track3 = new Track("track3", new Artist("Not The Beatles"), "album3", "/root/track3.mp3", null);
        final Track track4 = new Track("track4", new Artist("The Beatles"), "album4", "/root/track4.mp3", null);

        trackService.save(track1);
        trackService.save(track2);
        trackService.save(track3);
        trackService.save(track4);
        final List<Integer> trackIds = Arrays.asList(track1.getId(), track2.getId(), track3.getId(), track4.getId());
        dbHistoryPopulateService.populateTrackListened(trackIds, SecurityUtil.currentUser().getId());

        assertEquals(trackIds.size() - 1, trackService.getArtistMostPopularTracks("The Beatles").size());
    }

    @Test
    @WithUserDetails("user@example.com")
    public void testGetArtistMostPopularAlbums() throws Exception {

        final Track track1 = new Track("track1", new Artist("Johann Sebastian Bach"), "The Best", "/root/track1.mp3", null);
        final Track track2 = new Track("track2", new Artist("Johann Sebastian Bach"), "The Best", "/root/track2.mp3", null);
        final Track track3 = new Track("track3", new Artist("Johann Sebastian Bach"), "The Best", "/root/track3.mp3", null);
        final Track track4 = new Track("track4", new Artist("Johann Sebastian Bach"), "Not The Best", "/root/track4.mp3", null);

        trackService.save(track1);
        trackService.save(track2);
        trackService.save(track3);
        trackService.save(track4);
        final List<Integer> trackIds = Arrays.asList(track1.getId(), track2.getId(), track3.getId(), track4.getId());
        dbHistoryPopulateService.populateTrackListened(trackIds, SecurityUtil.currentUser().getId());

        assertEquals(2, trackService.getArtistMostPopularAlbums("Johann Sebastian Bach").size());
    }
}
