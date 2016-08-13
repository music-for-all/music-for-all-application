package com.musicforall.web.artist;

import com.musicforall.model.SearchTrackRequest;
import com.musicforall.model.Track;
import com.musicforall.services.track.TrackService;
import com.musicforall.web.tag.TagRestController;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@Validated
@RequestMapping("/artist")
public class ArtistRestController {
    private static final Logger LOG = LoggerFactory.getLogger(TagRestController.class);

    @Autowired
    private TrackService trackService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getArtists(@RequestParam("artistName") final String artistName,
                                     @RequestParam(value = "tags", required = false) final List<String> tags) {
        final SearchTrackRequest searchQuery = new SearchTrackRequest();
        searchQuery.setArtist(artistName);
        searchQuery.setTags(tags);
        final List<Track> tracks = trackService.getAllLike(searchQuery);

        final Set<String> artists = new HashSet<>();
        for (Track track : tracks) {
            artists.add(track.getArtist());
        }
        return new ResponseEntity<>(artists, HttpStatus.OK);
    }
}
