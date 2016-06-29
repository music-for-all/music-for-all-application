package com.musicforall.web;

import com.musicforall.model.Tag;
import com.musicforall.model.Track;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Evgeniy on 26.06.2016.
 */
@Controller
@RequestMapping("/tracks")
public class TrackController {
    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Track createTrack(@RequestParam("name") String name) {
        final Track track = new Track();
        track.setId(0);
        track.setName(name);
        return track;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpStatus deleteTrack(@PathVariable("id") Integer id) {
        final Track track = new Track();
        track.setId(id);
        track.setName(id + " deleted");
        return HttpStatus.NO_CONTENT;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Track getTrack(@PathVariable("id") Integer id) {
        final Track track = new Track();
        track.setId(id);
        track.setName("My Track");
        final Set<Tag> tags = new HashSet<Tag>() {
            {
                add(new Tag("HipHop"));
                add(new Tag("Rap"));
            }
        };
        track.setTags(tags);
        return track;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Set<Track> getTracks() {
        final Track track0 = new Track();
        track0.setId(0);
        track0.setName("My Track0");
        final Track track1 = new Track();
        track1.setId(1);
        track1.setName("My Track1");
        return new HashSet<Track>() {
            {
                add(track0);
                add(track1);
            }
        };
    }
}
