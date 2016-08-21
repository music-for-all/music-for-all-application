package com.musicforall.web.track;

import com.musicforall.history.handlers.events.TrackLikedEvent;
import com.musicforall.history.service.history.HistoryService;
import com.musicforall.model.Track;
import com.musicforall.model.User;
import com.musicforall.services.track.TrackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

import static com.musicforall.util.SecurityUtil.currentUser;

/**
 * @author Evgeniy on 26.06.2016.
 */
@RestController
@RequestMapping("/tracks")
public class TrackRestController {

    private static final Logger LOG = LoggerFactory.getLogger(TrackRestController.class);

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private TrackService trackService;

    @RequestMapping(method = RequestMethod.POST)
    public Track createTrack(@RequestParam("name") String name) {
        final Track track = new Track();
        track.setName(name);
        return trackService.save(track);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteTrack(@PathVariable("id") Integer id) {
        trackService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Track getTrack(@PathVariable("id") Integer id) {
        return trackService.get(id);
    }

    /**
     * Stores the like as a history event.
     *
     * @param id the id of the track to like
     * @return HTTP status code
     */
    @RequestMapping(value = "/like/{id}", method = RequestMethod.POST)
    public ResponseEntity like(@PathVariable Integer id) {

        final User user = currentUser();
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        publisher.publishEvent(new TrackLikedEvent(id, user.getId()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Retrieves the number of likes for the track with the given id.
     *
     * @param id the id of the track
     * @return the number of likes
     */
    @RequestMapping(value = "/like/{id}", method = RequestMethod.GET)
    public ResponseEntity<Long> getLikeCount(@PathVariable("id") Integer id) {

        final long numLikes = historyService.getLikeCount(id);

        return new ResponseEntity<>(numLikes, HttpStatus.OK);
    }


    @RequestMapping(value = "/popular", method = RequestMethod.GET)
    public Collection<Track> getByPopularity() {
        final List<Integer> popularTracksIds = historyService.getTheMostPopularTracks();
        return trackService.getAllById(popularTracksIds);
    }
}