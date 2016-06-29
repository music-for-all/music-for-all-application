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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNull;

/**
 * Created by Pukho on 28.06.2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        PlaylistTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
public class PlaylistServiceTest {

    @Autowired
    PlaylistService playlistService;

    @Autowired
    UserService userService;

    @Autowired
    TrackService trackService;

    @Test
    @WithMockUser
    public void testSavePlaylist() {
        final Playlist savedPlaylist = playlistService.save("playlist1");

        final Playlist playlist = playlistService.get(savedPlaylist.getId());
        assertNotNull(playlist);
    }

    @Test
    @WithMockUser(username = "user1", password = "password1")
    public void testGetAllUserPlaylist() {
        final Playlist playlist1 = playlistService.save("playlist1");
        final Playlist playlist2 = playlistService.save("playlist2");

        Integer userId = userService.getIdByName("user1");

        final Set<Playlist> allUsersPlaylists = playlistService.getAllUserPlaylist(userId);

        assertTrue(allUsersPlaylists.contains(playlistService.get(playlist1.getId())));
        assertTrue(allUsersPlaylists.size() == 2);
    }

    @Test
    @WithMockUser
    public void testDeletePlaylist() {
        final Playlist playlist = playlistService.save("playlist2");
        Integer playlistId = playlist.getId();
        playlistService.delete(playlistId);

        assertNull(playlistService.get(playlistId));
    }

    @Test
    @WithMockUser
    public void testGetTracks() {
        final Playlist playlist = playlistService.save("playlist_for_get");
        final Playlist expectedPlaylist = playlistService.get(playlist.getId());

        assertNotNull(expectedPlaylist);
        assertEquals(expectedPlaylist.getName(), playlist.getName());
        assertEquals(expectedPlaylist.getUser(), playlist.getUser());
    }

    @Test
    @WithMockUser(username = "user2", password = "password2")
    public void testGetAllTracksInPlaylist_AddTracksToPlaylist() {
        final Playlist playlist = playlistService.save("playlist1");
        Integer playlistId = playlist.getId();

        final Track track1 = new Track("track1", "location1");
        final Track track2 = new Track("track2", "location2");
        playlistService.addTracks(playlistId, new HashSet<>(Arrays.asList(track1, track2)));

        final Set<Track> tracksInPlaylist = playlistService.getAllTracksInPlaylist(playlistId);

        assertNotNull(tracksInPlaylist);
        Assert.assertTrue(tracksInPlaylist.size() == 2);
        assertTrue(tracksInPlaylist.contains(trackService.get(track1.getId())));
        assertTrue(tracksInPlaylist.contains(trackService.get(track2.getId())));
    }

}
