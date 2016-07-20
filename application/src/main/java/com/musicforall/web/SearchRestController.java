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
public class SearchRestController {

    @Autowired
    private TrackService trackService;

    private static final Logger LOG = LoggerFactory.getLogger(SearchRestController.class);

    public SearchRestController() {
        LOG.debug("Search RestController");
    }

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
    public ResponseEntity search(@RequestParam String title, @RequestParam String artist,
                                 @RequestParam String album, @RequestParam String tag) {

        /* Limit the query string length. */
        final int MAX_QUERY = 16;
        final int MIN_QUERY = 2;

        if (title != null && title.length() < MIN_QUERY || title.length() > MAX_QUERY) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if (artist != null &&artist.length() < MIN_QUERY || artist.length() > MAX_QUERY) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if (album != null && album.length() < MIN_QUERY || album.length() > MAX_QUERY) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if (tag != null && tag.length() < MIN_QUERY || tag.length() > MAX_QUERY) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        final List<Track> tracks = trackService.getAllByCriteria(title, artist, album, tag);

        return new ResponseEntity<List<Track>>(tracks, HttpStatus.OK);
    }
}
