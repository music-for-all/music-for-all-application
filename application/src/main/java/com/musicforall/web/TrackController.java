package com.musicforall.web;

import com.musicforall.model.Tag;
import com.musicforall.model.Track;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public Track createTrack(
            @RequestParam(value = "tags", required = false) Set<Tag> tags,
            @RequestParam("artist") String artist,
            @RequestParam("name") String name) {
        final Track track = new Track(tags, artist, name, "unknown");
        LOG.info(track.toString());
        return track;
    }

}
