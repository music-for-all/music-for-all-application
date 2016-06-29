package com.musicforall.services.playlist;

import com.musicforall.model.Playlist;
import com.musicforall.model.Track;
import com.musicforall.services.ServicesTestBoostrap;
import com.musicforall.services.track.TrackService;
import com.musicforall.services.user.UserService;
import com.musicforall.util.ServicesTestConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNull;

/**
 * Created by Pukho on 28.06.2016.
 */


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
public class PlaylistServiceTest {

    @Autowired
    PlaylistService playlistService;

    @Autowired
    UserService userService;

    @Autowired
    ServicesTestBoostrap boostraped;

    @Autowired
    TrackService trackService;

    @Before
    public void instalBoostrap(){
        boostraped.fillDatabase();
    }

    @Test
    @WithMockUser
    public void testSavePlaylist(){
        Integer playlistId = playlistService.save("playlist1");

        Playlist playlist = playlistService.get(playlistId);
        assertNotNull(playlist);
    }

    @Test
    @WithMockUser(username = "user1", password = "password1")
    public void testGetAllUserPlaylist(){
        Integer playlist1Id = playlistService.save("playlist1");
        Integer playlist2Id = playlistService.save("playlist2");

        Integer userId = userService.getIdByName("user1");

        Set<Playlist> allUsersPlaylists = playlistService.getAllUserPlaylist(userId);

        assertTrue(allUsersPlaylists.contains(playlistService.get(playlist1Id)));
        assertTrue(allUsersPlaylists.size()==2);
    }

    @Test
    @WithMockUser
    public void testDeletePlaylist(){
        Integer playlistId = playlistService.save("playlist2");
        playlistService.delete(playlistId);

        assertNull(playlistService.get(playlistId));
    }

    @Test
    @WithMockUser(username = "user2", password = "password2")
    public void testGetAllTracksInPlaylist(){
        Integer playlistId = playlistService.save("playlist1");

        Track track1 = new Track("track1", "location1");
        Track track2 = new Track("track2", "location2");
        playlistService.addTracks(playlistId, new HashSet<>(Arrays.asList(track1, track2)));

        Set<Track> tracksInPlaylist = playlistService.getAllTracksInPlaylist(playlistId);

        assertNotNull(tracksInPlaylist);
        Assert.assertTrue(tracksInPlaylist.size()==2);
        assertTrue(tracksInPlaylist.contains(trackService.get(track1.getId())));
        assertTrue(tracksInPlaylist.contains(trackService.get(track2.getId())));
    }

}
