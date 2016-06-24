package com.musicforall.services.songlist;

import com.musicforall.model.Playlist;
import com.musicforall.model.Track;
import com.musicforall.model.User;
import com.musicforall.services.song.SongService;
import com.musicforall.services.user.UserService;
import com.musicforall.util.ServicesTestConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Pukho on 22.06.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
public class SonglistServiceTest {
    @Autowired
    private SonglistService songlistService;

    @Autowired
    private UserService userService;

    @Autowired
    private SongService songService;

    @Autowired
    private SongListTestBoostrap boostrap;

    private Integer defaultUserId;
    private List<Integer> songlistsId;

    @Before
    public void setInformation() {
        boostrap.fillDatabase();
        /*testSonglistSave();
        testGetAllUserSonglist();*/
    }

    @After
    public void allDelete() {
        if (songlistService.getAllUserSonglist(defaultUserId).size() > 0) {
            testSonglistDelete();
        }
    }

    @Test
    public void testSonglistSave() {

        if (!userService.isUserExist(defaultUserId)) {
            User user = new User("Jhon", "1234567890", "Jhon@gmail.com");
            userService.save(user);
            defaultUserId = user.getId();

            songlistService.save(defaultUserId, "playlist1");
            songlistService.save(defaultUserId, "playlist2");
        }
    }

    @Test
    public void testGetAllUserSonglist() {
        Set<Playlist> songlists = songlistService.getAllUserSonglist(defaultUserId);

        songlistsId = new ArrayList<>();
        for (Playlist songlist :
                songlists) {
            assertNotNull(songlist);
            songlistsId.add(songlist.getId());
        }

        assertTrue(songlists.size() == 2);
    }

    @Test
    public void testGetAllSongsInSonglist() {
        songService.save(new Track("Sun", "path"), songlistsId.get(0));
        songService.save(new Track("Wild", "path2"), songlistsId.get(1));
        songService.save(new Track("Fire", "path3"), songlistsId.get(1));

        Set<Track> songs1 = songlistService.getAllSongsInSonglist(songlistsId.get(0));
        Set<Track> songs2 = songlistService.getAllSongsInSonglist(songlistsId.get(1));

        assertTrue(songs1.size() == 1);
        for (Track song :
                songs2) {
            assertTrue(song.getName().equals("Wild") ||
                    song.getName().equals("Fire"));
        }
    }

    @Test
    public void testSonglistDelete() {
        for (Integer id :
                songlistsId) {
            songlistService.delete(id);
        }
        userService.delete(defaultUserId);
    }

}
