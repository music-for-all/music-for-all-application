package com.musicforall.web.tag;

import com.musicforall.model.Tag;
import com.musicforall.services.recommendation.RecommendationService;
import com.musicforall.services.tag.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;

/**
 * @author Evgeniy on 01.08.2016.
 */
@RestController
@Validated
@RequestMapping("/tags")
public class TagRestController {
    private static final Logger LOG = LoggerFactory.getLogger(TagRestController.class);

    @Autowired
    private TagService tagService;

    @Autowired
    private RecommendationService recommendationService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getTags(@Size(min = 2, message = "The field must be at least {min} characters")
                                  @RequestParam("tagName") final String tagName) {
        final List<Tag> tags = tagService.getAllLike(tagName);
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @RequestMapping(value = "/popular", method = RequestMethod.GET)
    public ResponseEntity getPopularTags() {
        final Collection<Tag> tags = recommendationService.getPopularTags();
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }
}