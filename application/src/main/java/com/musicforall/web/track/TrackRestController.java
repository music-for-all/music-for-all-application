package com.musicforall.web.track;

import com.musicforall.common.Constants;
import com.musicforall.history.service.history.HistoryService;
import com.musicforall.model.Track;
import com.musicforall.model.user.User;
import com.musicforall.services.recommendation.RecommendationService;
import com.musicforall.services.track.TrackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static com.musicforall.util.SecurityUtil.currentUser;

/**
 * @author Evgeniy on 26.06.2016.
 */
@RestController
@RequestMapping("/tracks")
public class TrackRestController {

    private static final Logger LOG = LoggerFactory.getLogger(TrackRestController.class);

    @Autowired
    private HistoryService historyService;

    @Autowired
    private TrackService trackService;

    @Autowired
    private RecommendationService recommendationService;

    @RequestMapping(method = RequestMethod.POST)
    public Track createTrack(@RequestParam(Constants.NAME) String name) {
        final Track track = new Track();
        track.setName(name);
        return trackService.save(track);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteTrack(@PathVariable(Constants.ID) Integer id) {
        trackService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Track> getTrack(@PathVariable(Constants.ID) Integer id) {
        final Track track = trackService.get(id);
        if (track == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(track, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/like/{id}", method = RequestMethod.POST)
    public ResponseEntity like(@PathVariable Integer id) {
        final User user = currentUser();
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/like/{id}", method = RequestMethod.GET)
    public ResponseEntity<Long> getLikeCount(@PathVariable(Constants.ID) Integer id) {
        return new ResponseEntity<>(historyService.getLikeCount(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/recommended", method = RequestMethod.GET)
    public ResponseEntity<Collection<Track>> getRecommendedTracks() {
        final Collection<Track> tracks = recommendationService.getRecommendedTracks();
        return new ResponseEntity<>(tracks, HttpStatus.OK);
    }

    @RequestMapping(value = "/popular", method = RequestMethod.GET)
    public Collection<Track> getByPopularity() {
        return recommendationService.getMostPopularTracks();
    }

    @RequestMapping(value = "/topTracksOf/{artistName}", method = RequestMethod.GET)
    public Collection<Track> getArtistTracksByPopularity(@PathVariable String artistName) {
        return trackService.getArtistMostPopularTracks(artistName);
    }

    @RequestMapping(value = "/topAlbumsOf/{artistName}", method = RequestMethod.GET)
    public Collection<String> getArtistAlbumsByPopularity(@PathVariable String artistName) {
        return trackService.getArtistMostPopularAlbums(artistName);
    }
}