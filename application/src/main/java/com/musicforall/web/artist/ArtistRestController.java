package com.musicforall.web.artist;

import com.musicforall.model.Artist;
import com.musicforall.model.Tag;
import com.musicforall.services.artist.ArtistService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@Validated
@RequestMapping("/artist")
public class ArtistRestController {
    private static final Logger LOG = LoggerFactory.getLogger(TagRestController.class);

    @Autowired
    private TrackService trackService;

    @Autowired
    private ArtistService artistService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getArtists(@RequestParam("artistName") final String artistName,
                                     @RequestParam(value = "tags", required = false) final Set<Tag> tags) {
        final List<Artist> artists = artistService.getAllLike(artistName);
        final List<String> artistsNames = new ArrayList<>();
        for (final Artist artist : artists) {
            if (tags == null || artist.getTags().containsAll(tags)) {
                artistsNames.add(artist.getArtistName());
            }
        }
        return new ResponseEntity<>(artistsNames, HttpStatus.OK);
    }
}
