package com.musicforall.web;

import com.musicforall.model.Tag;
import com.musicforall.model.Track;
import com.musicforall.model.TrackSearchCriteria;
import com.musicforall.services.tag.TagService;
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
    @Autowired
    private TagService tagService;

    /**
     * Searches tracks by the specified criteria.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity search(@Valid TrackSearchCriteria searchCriteria, BindingResult bindingResult) {
        LOG.info(searchCriteria.toString());

        if (bindingResult.hasErrors()) {
            LOG.info(bindingResult.getAllErrors().toString());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        final List<Track> tracks = trackService.getAllLike(searchCriteria);

        return new ResponseEntity<>(tracks, HttpStatus.OK);
    }

    @RequestMapping(value = "/tags", method = RequestMethod.GET)
    public ResponseEntity getTags(final String tagName) {
        if (tagName == null || tagName.trim().isEmpty()) {
            LOG.error("tag name must not be empty");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        final List<Tag> tags = tagService.getTagsLike(tagName);
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }
}