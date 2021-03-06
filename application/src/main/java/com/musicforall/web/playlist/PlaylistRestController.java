package com.musicforall.web.playlist;

import com.musicforall.common.Constants;
import com.musicforall.model.Playlist;
import com.musicforall.services.playlist.PlaylistService;
import com.musicforall.util.SecurityUtil;
import com.musicforall.web.MainController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public Playlist createPlaylist(@RequestParam(Constants.NAME) String name) {
        return playlistService.save(name);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deletePlaylist(@PathVariable(Constants.ID) Integer id) {
        playlistService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Playlist getPlaylist(@PathVariable(Constants.ID) Integer id) {
        return playlistService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Playlist> getPlaylists() {
        return playlistService.getAllUserPlaylists(SecurityUtil.currentUser().getId());
    }

    @RequestMapping(value = "/{id}/add/{trackId}", method = RequestMethod.POST)
    public ResponseEntity addTrack(@PathVariable("id") Integer playlistId, @PathVariable Integer trackId) {
        playlistService.addTrack(playlistId, trackId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/remove/{trackId}", method = RequestMethod.DELETE)
    public ResponseEntity removeTrack(@PathVariable("id") Integer playlistId, @PathVariable Integer trackId) {
        playlistService.removeTrack(playlistId, trackId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/add/tracks", method = RequestMethod.POST)
    public ResponseEntity addTracks(@PathVariable("id") Integer playlistId,
                                    @RequestParam("tracksIds[]") Collection<Integer> tracksIds) {
        playlistService.addTracks(playlistId, tracksIds);
        return new ResponseEntity(HttpStatus.OK);
    }
}