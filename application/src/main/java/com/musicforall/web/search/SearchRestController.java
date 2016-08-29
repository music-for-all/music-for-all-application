package com.musicforall.web.search;

import com.musicforall.model.SearchTrackRequest;
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

import javax.validation.Valid;
import java.util.List;

/**
 * This RESTful Web service provides the track search facility.
 */
@Controller
@ResponseBody
@RequestMapping("/api/search")
public class SearchRestController {

    private static final Logger LOG = LoggerFactory.getLogger(SearchRestController.class);
    @Autowired
    private TrackService trackService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity search(@Valid SearchTrackRequest searchCriteria, BindingResult bindingResult) {
        LOG.info(searchCriteria.toString());

        if (bindingResult.hasErrors()) {
            LOG.info(bindingResult.getAllErrors().toString());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        final List<Track> tracks = trackService.getAllLike(searchCriteria);

        return new ResponseEntity<>(tracks, HttpStatus.OK);
    }
}