package com.musicforall.services.songlist;

import com.musicforall.model.Song;
import com.musicforall.model.Songlist;
import com.musicforall.model.User;
import com.musicforall.services.song.SongService;
import com.musicforall.services.songlist.SonglistService;
import com.musicforall.services.user.UserService;
import com.musicforall.util.JpaServicesTestConfig;
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
    public void setInformation(){
        boostrap.fillDatabase();
        /*testSonglistSave();
        testGetAllUserSonglist();*/
    }

    @After
    public void allDelete(){
        if (songlistService.getAllUserSonglist(defaultUserId).size()>0) {
            testSonglistDelete();
        }
    }

    @Test
    public void testSonglistSave(){

        if (!userService.isUserExist(defaultUserId)) {
            User user = new User("Jhon", "1234567890", "Jhon@gmail.com");
            userService.save(user);
            defaultUserId = user.getId();

            songlistService.save(defaultUserId, "playlist1");
            songlistService.save(defaultUserId, "playlist2");
        }
    }

    @Test
    public void testGetAllUserSonglist(){
        Set<Songlist> songlists = songlistService.getAllUserSonglist(defaultUserId);

        songlistsId = new ArrayList<>();
            for (Songlist songlist :
                    songlists) {
                assertNotNull(songlist);
                songlistsId.add(songlist.getId());
            }

        assertTrue(songlists.size()==2);
    }

    @Test
    public void testGetAllSongsInSonglist(){
        songService.save(new Song("Sun", "path"),   songlistsId.get(0));
        songService.save(new Song("Wild", "path2"), songlistsId.get(1));
        songService.save(new Song("Fire", "path3"), songlistsId.get(1));

        Set<Song> songs1 = songlistService.getAllSongsInSonglist(songlistsId.get(0));
        Set<Song> songs2 = songlistService.getAllSongsInSonglist(songlistsId.get(1));

        assertTrue(songs1.size()==1);
        for (Song song:
             songs2) {
            assertTrue(song.getName().equals("Wild") ||
                       song.getName().equals("Fire"));
        }
    }

    @Test
    public void testSonglistDelete(){
        for (Integer id:
                songlistsId) {
            songlistService.delete(id);
        }
        userService.delete(defaultUserId);
    }

}
