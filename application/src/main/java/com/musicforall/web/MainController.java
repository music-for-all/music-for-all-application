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

    public MainController() {
        LOG.debug("Main controller");
    }

    @RequestMapping("/main")
    public String welcome(Model model) {
        LOG.debug("Requested /main");
        return "main";
    }

    private static Set<Songlist> set;
    private static int id = 4;

    static {//only for test
        set = new HashSet<>();
        Set<Song> array;
        String location = "/home/andrey/MusicForAll";
        Songlist songlist = new Songlist(id++);
        songlist.setName("Nirvana");
        array = new HashSet<>();
        for (int i = 0; i < songlist.getId(); i++) {
            array.add(new Song("Nirvana" + i, location));
        }
        songlist.setSongs(array);
        set.add(songlist);
        songlist = new Songlist(id++);
        songlist.setName("Disturbed");
        array = new HashSet<>();
        for (int i = 0; i < songlist.getId(); i++) {
            array.add(new Song("Disturbed" + i, location));
        }
        songlist.setSongs(array);
        set.add(songlist);
        songlist = new Songlist(id++);
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
        LOG.debug("Requested /addPlaylist = " + name);

        Songlist songlist = new Songlist(id++);
        songlist.setName(name);
        set.add(songlist);
        return "main";
    }

    @RequestMapping(value = "/deletePlaylist", method = RequestMethod.POST)
    public String dummyDeletePlaylist(@RequestParam("deleteID") String id) {
        LOG.debug("Requested /deletePlaylist = " + id);

        Songlist songlist = new Songlist(this.id++);
        songlist.setName(id + " deleted");
        set.add(songlist);
        return "main";
    }

    @RequestMapping(value = "/getPlayList", method = RequestMethod.GET)
    @ResponseBody
    public Set<Song> dummyGetPlayList(@RequestParam("playlistID") Integer id) {
        LOG.debug("Requested /getPlayList id = " + id);

        Songlist playlist;
        Iterator<Songlist> iterator = set.iterator();
        while (iterator.hasNext()){
            if ((playlist = iterator.next()).getId().equals(id)){
                return playlist.getSongs() != null ? playlist.getSongs() : new HashSet<>();
            }
        }
        return new HashSet<>();
    }
}
