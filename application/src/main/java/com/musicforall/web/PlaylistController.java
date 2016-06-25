package com.musicforall.web;

import com.musicforall.model.Playlist;
import com.musicforall.model.Track;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

/**
 * @author ENikolskiy.
 */
@Controller
@RequestMapping("/playlist")
public class PlaylistController {
    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);
    private static Set<Playlist> set;

    /*
    * only for test
     */
    static {
        final int collectionSize = 3;
        set = new HashSet<>();
        Set<Track> array;
        String location = "/home/andrey/MusicForAll";
        Playlist songlist = new Playlist();
        songlist.setName("Nirvana");
        songlist.setId(0);
        array = new HashSet<>();
        for (int i = 0; i < collectionSize; i++) {
            array.add(new Track("Nirvana" + i, location));
        }
        songlist.setTracks(array);
        set.add(songlist);
        songlist = new Playlist();
        songlist.setName("Disturbed");
        songlist.setId(1);
        array = new HashSet<>();
        for (int i = 0; i < collectionSize; i++) {
            array.add(new Track("Disturbed" + i, location));
        }
        songlist.setTracks(array);
        set.add(songlist);
        songlist = new Playlist();
        songlist.setName("Rob Zombie");
        songlist.setId(2);
        array = new HashSet<>();
        for (int i = 0; i < collectionSize; i++) {
            array.add(new Track("Rob Zombie" + i, location));
        }
        songlist.setTracks(array);
        set.add(songlist);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Integer dummyAddPlaylist(@RequestParam("name") String name) {
        Playlist songlist = new Playlist();
        songlist.setName(name);
        songlist.setId(0);
        set.add(songlist);
        return songlist.getId();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Integer dummyDeletePlaylist(@PathVariable("id") Integer id) {
        Playlist songlist = new Playlist();
        songlist.setId(id);
        songlist.setName(id + " deleted");
        set.add(songlist);
        return id;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Playlist dummyGetPlayList(@RequestParam("id") Integer id) {
        for (Playlist playlist : set) {
            if (playlist.getId().equals(id)) {
                if (playlist.getTracks() == null || playlist.getTracks().isEmpty()) {
                    playlist.setTracks(new HashSet<Track>() {
                        {
                            add(new Track("DefaultName1", "DefaultLocation1"));
                            add(new Track("DefaultName2", "DefaultLocation2"));
                        }
                    });
                }
                return playlist;
            }
        }
        return null;
    }
}
