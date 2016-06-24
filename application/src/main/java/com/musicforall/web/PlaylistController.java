package com.musicforall.web;

import com.musicforall.model.Song;
import com.musicforall.model.Songlist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashSet;
import java.util.Set;

/**
 * @author ENikolskiy.
 */
@Controller
@RequestMapping("/playlist")
public class PlaylistController {
    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);
    private static Set<Songlist> set;
    private static int id;

    /*
    * only for test
     */
    static {
        id = 0;
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

    @RequestMapping(value = "/playlist/create", method = RequestMethod.POST)
    @ResponseBody
    public Integer dummyAddPlaylist(@RequestParam("name") String name) {

        Songlist songlist = new Songlist(id++);
        songlist.setName(name);
        set.add(songlist);
        return songlist.getId();
    }

    @RequestMapping(value = "/playlist/delete", method = RequestMethod.POST)
    @ResponseBody
    public Integer dummyDeletePlaylist(@RequestParam("id") Integer id) {

        Songlist songlist = new Songlist(this.id++);
        songlist.setName(id + " deleted");
        set.add(songlist);
        return id;
    }

    @RequestMapping(value = "/playlist", method = RequestMethod.GET)
    @ResponseBody
    public Set<Song> dummyGetPlayList(@RequestParam("id") Integer id) {

        for (Songlist playlist : set) {
            if (playlist.getId().equals(id)) {
                if (playlist.getSongs() != null) {
                    return playlist.getSongs();
                } else {
                    return new HashSet<>();
                }
            }
        }
        return new HashSet<Song>() {{
            add(new Song("DefaultName1", "DefaultLocation1"));
            add(new Song("DefaultName2", "DefaultLocation2"));
        }};
    }

    @RequestMapping(value = "/getPlayLists", method = RequestMethod.GET)
    @ResponseBody
    public Set<Songlist> dummyGetPlayLists() {
        LOG.debug("Requested /getPlayLists");

        return set;
    }


}
