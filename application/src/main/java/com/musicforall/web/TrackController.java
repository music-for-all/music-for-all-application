package com.musicforall.web;

import com.musicforall.model.Tag;
import com.musicforall.model.Track;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;

/**
 * @author Evgeniy on 26.06.2016.
 */
@Controller
@RequestMapping("/track")
public class TrackController {
    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Integer createTrack(@RequestParam("name") String name) {
        Track track = new Track();
        track.setId(0);
        track.setName(name);
        return track.getId();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Integer deleteTrack(@PathVariable("id") Integer id) {
        Track track = new Track();
        track.setId(id);
        track.setName(id + " deleted");
        return id;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Track getTrack(@RequestParam("id") Integer id) {
        Track track = new Track();
        track.setId(id);
        track.setName("My Track");
        HashSet<Tag> tags = new HashSet<Tag>() {
            {
                add(new Tag("HipHop"));
                add(new Tag("Rap"));
            }
        };
        track.setTags(tags);
        return track;
    }
}
