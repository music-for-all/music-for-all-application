package com.musicforall.web;

import com.musicforall.model.Track;
import com.musicforall.model.TrackSearch;
import com.musicforall.services.track.TrackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
     * Searches tracks by the specified criteria.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity search(@ModelAttribute TrackSearch trackSearch, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        final List<Track> tracks = trackService.getAllLike(trackSearch);

        for (Track track : tracks) {
            if (track.getTags().contains(trackSearch.getTag())) {
                tracks.remove(track);
            }
        }
        return new ResponseEntity<List<Track>>(tracks, HttpStatus.OK);
    }
}
