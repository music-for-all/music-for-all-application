package com.musicforall.web;

import com.musicforall.model.Playlist;
import com.musicforall.model.Track;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ENikolskiy.
 */
@RestController
@RequestMapping("/playlists")
public class PlaylistRestController {
    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);

    @RequestMapping(method = RequestMethod.POST)
    public Playlist createPlaylist(@RequestParam("name") String name) {
        final Playlist playlist = new Playlist();
        playlist.setId(name.hashCode());
        playlist.setName(name);
        return playlist;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public HttpStatus deletePlaylist(@PathVariable("id") Integer id) {
        final Playlist playlist = new Playlist();
        playlist.setId(id);
        playlist.setName(id + " deleted");
        return HttpStatus.NO_CONTENT;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Playlist getPlaylist(@PathVariable("id") Integer id) {
        final Playlist playlist = new Playlist();
        playlist.setId(id);
        playlist.setName("My Playlist");
        final Set<Track> tracks = new HashSet<Track>() {
            {
                final Track track1 = new Track("First Track", "Com+Truise+-+Cyanide+Sisters.mp3");
                track1.setId(1);
                add(track1);
                final Track track2 = new Track("Second Track", "Best+Coast+-+The+Only+Place.mp3");
                track2.setId(2);
                add(track2);
            }
        };
        playlist.setTracks(tracks);
        return playlist;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Playlist> getPlaylists() {
        final Playlist playlist0 = new Playlist();
        playlist0.setId(0);
        playlist0.setName("My Playlist0");
        final Playlist playlist1 = new Playlist();
        playlist1.setId(1);
        playlist1.setName("My Playlist1");
        return new HashSet<Playlist>() {
            {
                add(playlist0);
                add(playlist1);
            }
        };
    }
}
