package com.musicforall.web;

import com.musicforall.model.Track;
import com.musicforall.services.track.TrackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @author Evgeniy on 26.06.2016.
 */
@RestController
@RequestMapping("/tracks")
public class TrackRestController {

    private static final Logger LOG = LoggerFactory.getLogger(TrackRestController.class);

    @Autowired
    private TrackService trackService;

    @RequestMapping(method = RequestMethod.POST)
    public Track createTrack(@RequestParam("name") String name) {
        final Track track = new Track();
        track.setName(name);
        return trackService.save(track);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public HttpStatus deleteTrack(@PathVariable("id") Integer id) {
        trackService.delete(id);
        return HttpStatus.NO_CONTENT;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Track getTrack(@PathVariable("id") Integer id) {
        return trackService.get(id);
    }
}