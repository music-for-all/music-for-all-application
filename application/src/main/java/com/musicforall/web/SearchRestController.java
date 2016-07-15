package com.musicforall.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musicforall.model.Track;
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
        final String location = "/home/andrey/MusicForAll";
        array.add(new Track(search, location));
        if (!listCategory.isEmpty()) {
            Track track;
            track = new Track(listCategory.get(0), location);
            array.add(track);
        }
        return array;
    }
}
