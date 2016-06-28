package com.musicforall.web;

import com.musicforall.model.Playlist;
import com.musicforall.model.Track;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;

/**
 * @author ENikolskiy.
 */
@Controller
@RequestMapping("/playlist")
public class PlaylistController {
    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Integer createPlaylist(@RequestParam("name") String name) {
        Playlist playlist = new Playlist();
        playlist.setId(0);
        playlist.setName(name);
        return playlist.getId();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Integer deletePlaylist(@PathVariable("id") Integer id) {
        Playlist playlist = new Playlist();
        playlist.setId(id);
        playlist.setName(id + " deleted");
        return id;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Playlist getPlaylist(@PathVariable("id") Integer id) {
        Playlist playlist = new Playlist();
        playlist.setId(id);
        playlist.setName("My Playlist");
        HashSet<Track> tracks = new HashSet<Track>() {
            {
                add(new Track("First Track", "First Location"));
                add(new Track("Second Track", "Second Location"));
            }
        };
        playlist.setTracks(tracks);
        return playlist;
    }
}
