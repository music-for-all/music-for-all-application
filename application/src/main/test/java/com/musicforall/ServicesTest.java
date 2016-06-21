package com.musicforall;

import com.musicforall.common.dao.Dao;
import com.musicforall.model.Song;
import com.musicforall.model.Songlist;
import com.musicforall.model.Tag;
import com.musicforall.model.User;
import com.musicforall.services.SongService;
import com.musicforall.services.SonglistService;
import com.musicforall.services.UserService;
import com.musicforall.util.JpaServicesTestConfig;
import com.musicforall.util.ServicesTestConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Pukho on 19.06.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {JpaServicesTestConfig.class,
                                                                                  ServicesTestConfig.class})
public class ServicesTest {

    @Autowired
    private UserService userService;

    @Autowired
    private SongService songService;

    @Autowired
    private SonglistService songlistService;

    private Integer defaultUserId;
    private List<Integer> listOfSonglistId = new ArrayList<>();

    @Before
    public void testAddInf() {
        User user = new User("Masha", "123456789", "Masha@gmail.com");
        userService.save(user);
        defaultUserId = user.getId();

        songlistService.save(defaultUserId, "All");
        songlistService.save(defaultUserId, "All2");

        Set<Songlist> songlists = userService.getAllUserSonglist(user);
        for (Songlist songlist :
                songlists) {
            listOfSonglistId.add(songlist.getId());
        }
    }

    @After
    public void deleteAll(){

        for (Integer id:
                listOfSonglistId) {
            songlistService.delete(id);
        }
        userService.delete(defaultUserId);
    }

    @Test
    public void testGetAllUserSonglist() {

        User user = userService.get(defaultUserId);
        Set<Songlist> songlists = userService.getAllUserSonglist(user);
        assertTrue(songlists.size() == 2);

    }

    @Test
    public void testGetAllSongInSonglistId(){

        songService.save(new Song("Sun", "path"), listOfSonglistId.get(1));
        songService.save(new Song("Wild", "path2"), listOfSonglistId.get(1));
        songService.save(new Song("Fire", "path3"), listOfSonglistId.get(1));

        Set<Song> songs = songlistService.getAllSongsInSonglist(listOfSonglistId.get(1));
        assertTrue(songs.size()==3);
    }


    @Test
    public void testTags() {
        Song song = new Song("song1", "path1");
        Song song2 = new Song("song2", "path2");
        songService.save(song); songService.save(song2);
        Integer songId = song.getId();
        Integer song2Id = song2.getId();

        songService.addTag(songId, "rock");
        songService.addTag(songId, "rock-n-roll");
        songService.addTag(song2Id, "rock");

        assertTrue(songService.getAllSongTag(songId).size()==2);
        assertTrue(songService.getAllSongTag(song2Id).size()==1);
        assertTrue(songService.getAllTags().size() == 2);
    }
}
