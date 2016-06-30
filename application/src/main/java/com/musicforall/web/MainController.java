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

    private static final String MAIN = "main";

    public MainController() {
        LOG.debug("Main controller");
    }

    @RequestMapping("/main")
    public String welcome(Model model) {
        LOG.debug("Requested /main");
        return MAIN;
    }

    @RequestMapping(value = "/getPlayLists", method = RequestMethod.GET)
    @ResponseBody
    public Set<Playlist> dummyGetPlayLists() {
        LOG.debug("Requested /getPlayLists");

        return new HashSet<>();
    }

    @RequestMapping(value = "/addPlaylist", method = RequestMethod.POST)
    public String dummyAddPlaylist(@RequestParam("playlist") String name) {
        LOG.debug("Requested /addPlaylist");

        Playlist playlist = new Playlist();
        playlist.setName(name);
        return MAIN;
    }

    @RequestMapping(value = "/deletePlaylist", method = RequestMethod.POST)
    public String dummyDeletePlaylist(@RequestParam("deleteID") Integer id) {
        LOG.debug("Requested /deletePlaylist");

        Playlist playlist = new Playlist();
        playlist.setName(id + " deleted");
        return MAIN;
    }

    @RequestMapping(value = "/getPlayList", method = RequestMethod.GET)
    @ResponseBody
    public Set<Track> dummyGetPlayList(@RequestParam("playlistID") Integer id) {
        LOG.debug("Requested /getPlayList id");

        return new HashSet<>();
    }

    @RequestMapping(value = "/deleteSong", method = RequestMethod.POST)
    public String dummyDeleteSong(@RequestParam("deleteID") Integer id) {
        LOG.debug("Requested /deleteSong");

        return MAIN;
    }
}
