package com.musicforall.services.playlist;

import com.musicforall.model.Playlist;
import com.musicforall.model.Track;
import com.musicforall.services.track.TrackService;
import com.musicforall.services.user.UserService;
import com.musicforall.util.ServicesTestConfig;
import org.junit.Assert;
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

import static org.junit.Assert.*;

/**
 * Created by Pukho on 28.06.2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        PlaylistTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
@ActiveProfiles("dev")
public class PlaylistServiceTest {

    public static final String PLAYLIST_1 = "playlist1";

    public static final String PLAYLIST_2 = "playlist2";

    public static final String USER_EMAIL_1 = "user1@gmail.com";

    public static final String USER_EMAIL_2 = "user2@example.com";

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private UserService userService;

    @Autowired
    private TrackService trackService;

    @Test
    @WithUserDetails(USER_EMAIL_2)
    public void testSavePlaylist() {
        final Playlist savedPlaylist = playlistService.save(PLAYLIST_1);

        final Playlist playlist = playlistService.get(savedPlaylist.getId());
        assertNotNull(playlist);
    }

    @Test
    @WithUserDetails(USER_EMAIL_1)
    public void testGetAllUserPlaylist() {
        final Playlist playlist1 = playlistService.save(PLAYLIST_1);
        playlistService.save(PLAYLIST_2);

        final Integer userId = userService.getIdByEmail("user1@gmail.com");

        final Set<Playlist> allUsersPlaylists = playlistService.getAllUserPlaylists(userId);

        assertTrue(allUsersPlaylists.contains(playlistService.get(playlist1.getId())));
    }

    @Test
    @WithUserDetails(USER_EMAIL_1)
    public void testDeletePlaylist() {
        final Playlist playlist = playlistService.save(PLAYLIST_2);
        final Integer playlistId = playlist.getId();
        playlistService.delete(playlistId);

        assertNull(playlistService.get(playlistId));
    }

    @Test
    @WithUserDetails("user@example.com")
    public void testGetTracks() {
        final Playlist playlist = playlistService.save("playlist_for_get");
        final Playlist expectedPlaylist = playlistService.get(playlist.getId());

        assertNotNull(expectedPlaylist);
        assertEquals(expectedPlaylist.getName(), playlist.getName());
        assertEquals(expectedPlaylist.getUser(), playlist.getUser());
    }

    @Test
    @WithUserDetails(USER_EMAIL_1)
    public void testGetAllByIds() {
        final Playlist playlist1 = playlistService.save(PLAYLIST_1);
        final Playlist playlist2 = playlistService.save(PLAYLIST_2);
        final List<Integer> ids = Arrays.asList(playlist1.getId(), playlist2.getId());

        final Collection<Playlist> foundPlaylists = playlistService.getAllByIds(ids);
        assertEquals(foundPlaylists.size(), ids.size());
        assertTrue(foundPlaylists.stream().allMatch(p -> ids.contains(p.getId())));
    }

    @Test
    public void testGetAllByEmptyIds() {
        final Collection<Playlist> playlist = playlistService.getAllByIds(Collections.emptyList());
        assertTrue(playlist.isEmpty());
    }

    @Test
    public void testGetAllByNullIds() {
        final Collection<Playlist> playlist = playlistService.getAllByIds(null);
        assertTrue(playlist.isEmpty());
    }

    @Test
    @WithUserDetails(USER_EMAIL_2)
    public void testGetAllTracksInPlaylistAddTracksToPlaylist() {
        final Playlist playlist = playlistService.save(PLAYLIST_1);
        final Integer playlistId = playlist.getId();

        trackService.saveAll(Arrays.asList(
                new Track("track1", "location1"),
                new Track("track2", "location2")));

        final List<Track> tracksInDb = trackService.findAll();
        final Set<Integer> tracksIds = tracksInDb.stream()
                .map(Track::getId)
                .collect(Collectors.toSet());

        playlistService.addTracks(playlistId, tracksIds);

        final Set<Track> tracksInPlaylist = playlistService.getAllTracksInPlaylist(playlistId);

        assertNotNull(tracksInPlaylist);
        Assert.assertEquals(tracksInDb.size(), tracksInPlaylist.size());

        for (Track track : tracksInDb) {
            assertTrue(tracksInPlaylist.contains(trackService.get(track.getId())));
        }

        for (Integer trackId : tracksIds) {
            playlistService.removeTrack(playlistId, trackId);
        }
    }

    @Test
    @WithUserDetails("user@example.com")
    public void testAddTrack() {

        final Playlist playlist = playlistService.save("testAddTrack");
        final Integer playlistId = playlist.getId();
        final Integer trackId = trackService.save(new Track("testAddTrack", "location")).getId();

        playlistService.addTrack(playlistId, trackId);
        assertEquals(1, playlistService.get(playlistId).getTracks().size());

        playlistService.removeTrack(playlistId, trackId);
    }

    @Test
    @WithUserDetails("user@example.com")
    public void testRemoveTrack() {

        final Playlist playlist = playlistService.save("test");
        final Integer playlistId = playlist.getId();
        final Integer trackId = trackService.save(new Track("testTrack", "location")).getId();

        playlistService.addTrack(playlistId, trackId);
        assertEquals(1, playlistService.get(playlistId).getTracks().size());

        playlistService.removeTrack(playlistId, trackId);
        assertEquals(0, playlistService.get(playlistId).getTracks().size());
    }
}
