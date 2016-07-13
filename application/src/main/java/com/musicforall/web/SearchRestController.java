package com.musicforall.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musicforall.model.Track;
import com.musicforall.model.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class SearchRestController {

    private static final Logger LOG = LoggerFactory.getLogger(SearchRestController.class);

    public SearchRestController() {
        LOG.debug("Search RestController");
    }

    @RequestMapping(value = "/searchQuery", method = RequestMethod.GET)
    public Set<Track> dummyFind(@RequestParam("search") String search,
                                @RequestParam("category") String jsonCategory)
            throws IOException {
        LOG.debug("Requested /searchQuery");
        final ObjectMapper objectMapper = new ObjectMapper();
        final List<String> listCategory = objectMapper.readValue(
                jsonCategory,
                objectMapper.getTypeFactory().constructCollectionType(
                        List.class, String.class));

        final Set<Track> array = new HashSet<>();
        final Set<Tag> tag = new HashSet<>();
        final String location = "/home/andrey/MusicForAll";
        final Tag t = new Tag();
        t.setName("music");
        tag.add(t);
        array.add(new Track(tag, search, location));
        for (int i = 0; i < listCategory.size(); i++) {
            array.add(new Track(tag, listCategory.get(i), location));
        }
        return array;
    }
}
