package com.musicforall.web;

import com.musicforall.model.Playlist;
import com.musicforall.model.Track;
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

    private static Set<Playlist> set;

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
        Set<Track> array;
        String location = "/home/andrey/MusicForAll";
        Playlist playlist = new Playlist();
        playlist.setName("Nirvana");
        array = new HashSet<>();
        for (int i = 0; i < new Integer("1"); i++) {
            array.add(new Track("Nirvana" + i, location));
        }
        playlist.setTracks(array);
        set.add(playlist);
        playlist = new Playlist();
        playlist.setName("Disturbed");
        array = new HashSet<>();
        for (int i = 0; i < new Integer("2"); i++) {
            array.add(new Track("Disturbed" + i, location));
        }
        playlist.setTracks(array);
        set.add(playlist);
        playlist = new Playlist();
        playlist.setName("Rob Zombie");
        array = new HashSet<>();
        for (int i = 0; i < new Integer("3"); i++) {
            array.add(new Track("Rob Zombie" + i, location));
        }
        playlist.setTracks(array);
        set.add(playlist);
    }

    @RequestMapping(value = "/getPlayLists", method = RequestMethod.GET)
    @ResponseBody
    public Set<Playlist> dummyGetPlayLists() {
        LOG.debug("Requested /getPlayLists");

        return set;
    }

    @RequestMapping(value = "/addPlaylist", method = RequestMethod.POST)
    public String dummyAddPlaylist(@RequestParam("playlist") String name) {
        LOG.debug("Requested /addPlaylist");

        Playlist playlist = new Playlist();
        playlist.setName(name);
        set.add(playlist);
        return MAIN;
    }

    @RequestMapping(value = "/deletePlaylist", method = RequestMethod.POST)
    public String dummyDeletePlaylist(@RequestParam("deleteID") Integer id) {
        LOG.debug("Requested /deletePlaylist");

        Playlist playlist = new Playlist();
        playlist.setName(id + " deleted");
        set.add(playlist);
        return MAIN;
    }

    @RequestMapping(value = "/getPlayList", method = RequestMethod.GET)
    @ResponseBody
    public Set<Track> dummyGetPlayList(@RequestParam("playlistID") Integer id) {
        LOG.debug("Requested /getPlayList id");

        Playlist playlist;
        Iterator<Playlist> iterator = set.iterator();
        while (iterator.hasNext()) {
            playlist = iterator.next();
            if (playlist.getId().equals(id)) {
                if (playlist.getTracks() != null) {
                    return playlist.getTracks();
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
