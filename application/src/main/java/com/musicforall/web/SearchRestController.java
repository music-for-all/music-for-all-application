package com.musicforall.web;

import com.musicforall.model.SearchCriteria;
import com.musicforall.model.Tag;
import com.musicforall.model.Track;
import com.musicforall.services.track.TrackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashSet;
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
        trackService.save(new Track("Track 1", "Track 1", "Artist 1", null, "/track1.mp3",
                new HashSet<Tag>(Arrays.asList(new Tag("TagA")))));
        trackService.save(new Track("Track 2", "Track 2", "Artist 2", "Album 2", "/track2.mp3", null));
    }

    /**
     * Searches tracks by the specified criteria.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity search(@Valid SearchCriteria searchCriteria, BindingResult bindingResult) {

        LOG.info(searchCriteria.toString());

        if (bindingResult.hasErrors()) {
            LOG.info(bindingResult.getAllErrors().toString());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        final List<Track> tracks = trackService.getAllLike(searchCriteria);

        return new ResponseEntity<List<Track>>(tracks, HttpStatus.OK);
    }
}