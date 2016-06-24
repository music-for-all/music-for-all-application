package com.musicforall.web;

import com.musicforall.model.Song;
import com.musicforall.model.Songlist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Controller
public class MainController {

    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);

    private static Set<Songlist> set;

    private static final String MAIN = "main";

    public MainController() {
        LOG.debug("Main controller");
    }

    @RequestMapping("/main")
    public String welcome(Model model) {
        LOG.debug("Requested /main");
        return MAIN;
    }


    /*
    * only for test
     */
    static {
        set = new HashSet<>();
        Set<Song> array;
        String location = "/home/andrey/MusicForAll";
        Songlist songlist = new Songlist();
        songlist.setName("Nirvana");
        array = new HashSet<>();
        for (int i = 0; i < songlist.getId(); i++) {
            array.add(new Song("Nirvana" + i, location));
        }
        songlist.setSongs(array);
        set.add(songlist);
        songlist = new Songlist();
        songlist.setName("Disturbed");
        array = new HashSet<>();
        for (int i = 0; i < songlist.getId(); i++) {
            array.add(new Song("Disturbed" + i, location));
        }
        songlist.setSongs(array);
        set.add(songlist);
        songlist = new Songlist();
        songlist.setName("Rob Zombie");
        array = new HashSet<>();
        for (int i = 0; i < songlist.getId(); i++) {
            array.add(new Song("Rob Zombie" + i, location));
        }
        songlist.setSongs(array);
        set.add(songlist);
    }

    @RequestMapping(value = "/getPlayLists", method = RequestMethod.GET)
    @ResponseBody
    public Set<Songlist> dummyGetPlayLists() {
        LOG.debug("Requested /getPlayLists");

        return set;
    }

    @RequestMapping(value = "/addPlaylist", method = RequestMethod.POST)
    public String dummyAddPlaylist(@RequestParam("playlist") String name) {
        LOG.debug("Requested /addPlaylist");

        Songlist songlist = new Songlist();
        songlist.setName(name);
        set.add(songlist);
        return MAIN;
    }

    @RequestMapping(value = "/deletePlaylist", method = RequestMethod.POST)
    public String dummyDeletePlaylist(@RequestParam("deleteID") Integer id) {
        LOG.debug("Requested /deletePlaylist");

        Songlist songlist = new Songlist();
        songlist.setName(id + " deleted");
        set.add(songlist);
        return MAIN;
    }

    @RequestMapping(value = "/getPlayList", method = RequestMethod.GET)
    @ResponseBody
    public Set<Song> dummyGetPlayList(@RequestParam("playlistID") Integer id) {
        LOG.debug("Requested /getPlayList id");

        Songlist playlist;
        Iterator<Songlist> iterator = set.iterator();
        while (iterator.hasNext()) {
            playlist = iterator.next();
            if (playlist.getId().equals(id)) {
                if (playlist.getSongs() != null) {
                    return playlist.getSongs();
                } else {
                    return new HashSet<>();
                }
            }
        }
        return new HashSet<>();
    }

    @RequestMapping(value = "/deleteSong", method = RequestMethod.POST)
    public String dummyDeleteSong(@RequestParam("deleteID") Integer id) {
        LOG.debug("Requested /deleteSong");

        return MAIN;
    }
}
