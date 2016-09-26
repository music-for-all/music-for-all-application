package com.musicforall.web.artist;

import com.musicforall.model.Artist;
import com.musicforall.model.SearchArtistRequest;
import com.musicforall.services.artist.ArtistService;
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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/artist")
public class ArtistRestController {
    private static final Logger LOG = LoggerFactory.getLogger(TagRestController.class);

    @Autowired
    private ArtistService artistService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getArtists(@RequestParam("artistName") final String artistName,
                                     @RequestParam(value = "tags", required = false) final List<String> tags) {
        final SearchArtistRequest searchCriteria = new SearchArtistRequest(artistName, tags);
        final List<Artist> artists = artistService.getAllLike(searchCriteria);
        final List<String> artistsNames = artists.stream().map(Artist::getArtistName).collect(Collectors.toList());

        return new ResponseEntity<>(artistsNames, HttpStatus.OK);
    }
}
