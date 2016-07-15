package com.musicforall.web;

import com.musicforall.model.Track;
import com.musicforall.services.track.TrackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * This RESTful Web service provides the track search facility.
 */
@Controller
@ResponseBody
@RequestMapping("/api/search")
public class TrackSearchController {

    @Autowired
    private TrackService trackService;

    private static final Logger LOG = LoggerFactory.getLogger("TrackSearchController");

    @PostConstruct
    private void addSampleRecords() {
        LOG.info("Post construct");
        trackService.save(new Track("Track 1", "/track1.mp3"));
        trackService.save(new Track("Track 2", "/track2.mp3"));
    }

    /**
     * Searches tracks by the specified <code>query</code> in the specified <code>category</code>.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity search(@RequestParam String query, @RequestParam String category) {

        /* Limit the query string length. */
        final int MAX_QUERY = 16;
        final int MIN_QUERY = 2;

        if (query.length() < MIN_QUERY || query.length() > MAX_QUERY) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        List<Track> tracks;

        /* Perform search in the appropriate repository corresponding the category. */
        switch (category) {
            case "title":
                tracks = trackService.getAllByName(query);
                break;
            case "artist":
            case "album":
            default:
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<List<Track>>(tracks, HttpStatus.OK);
    }
}
