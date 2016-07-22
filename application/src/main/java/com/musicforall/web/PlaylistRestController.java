package com.musicforall.web;

import com.musicforall.model.Playlist;
import com.musicforall.services.playlist.PlaylistService;
import com.musicforall.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * @author ENikolskiy.
 */
@RestController
@RequestMapping("/playlists")
public class PlaylistRestController {

    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private PlaylistService playlistService;

    @RequestMapping(method = RequestMethod.POST)
    public Playlist createPlaylist(@RequestParam("name") String name) {
        return playlistService.save(name);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public HttpStatus deletePlaylist(@PathVariable("id") Integer id) {
        playlistService.delete(id);
        return HttpStatus.NO_CONTENT;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Playlist getPlaylist(@PathVariable("id") Integer id) {
        return playlistService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Playlist> getPlaylists() {
        return playlistService.getAllUserPlaylist(UserUtil.getCurrentUser().getId());
    }
}