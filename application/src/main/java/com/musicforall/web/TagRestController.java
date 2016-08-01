package com.musicforall.web;

import com.musicforall.model.Tag;
import com.musicforall.services.tag.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Evgeniy on 01.08.2016.
 */
@RestController
@RequestMapping("/tags")
public class TagRestController {
    private static final Logger LOG = LoggerFactory.getLogger(TagRestController.class);

    @Autowired
    private TagService tagService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getTags(@RequestParam("tagName") final String tagName) {
        if (tagName == null || tagName.trim().isEmpty()) {
            LOG.error("tag name must not be empty");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        final List<Tag> tags = tagService.getAllLike(tagName);
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }
}
