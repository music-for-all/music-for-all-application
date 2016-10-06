package com.musicforall.web.history;

import com.musicforall.history.handlers.events.EventType;
import com.musicforall.history.handlers.events.PlaylistEvent;
import com.musicforall.history.handlers.events.TrackEvent;
import com.musicforall.model.User;
import com.musicforall.services.playlist.PlaylistService;
import com.musicforall.services.track.TrackService;
import com.musicforall.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ENikolskiy.
 */
@RestController
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private TrackService trackService;

    @RequestMapping(value = "/track/liked", method = RequestMethod.POST)
    public void trackLiked(@RequestParam("id") final Integer trackId) {
        fireTrackEvent(trackId, EventType.TRACK_LIKED);
    }

    @RequestMapping(value = "/track/listened", method = RequestMethod.POST)
    public void trackListened(@RequestParam("id") final Integer trackId) {
        fireTrackEvent(trackId, EventType.TRACK_LISTENED);
    }

    @RequestMapping(value = "/track/added", method = RequestMethod.POST)
    public void trackAdded(@RequestParam("trackId") final Integer trackId,
                           @RequestParam("playlistId") final Integer playlistId) {
        fireTrackEvent(trackId, playlistId, EventType.TRACK_ADDED);
    }

    @RequestMapping(value = "/track/deleted", method = RequestMethod.POST)
    public void trackDeleted(@RequestParam("trackId") final Integer trackId,
                             @RequestParam("playlistId") final Integer playlistId) {
        fireTrackEvent(trackId, playlistId, EventType.TRACK_DELETED);
    }

    @RequestMapping(value = "/playlist/added", method = RequestMethod.POST)
    public void playlistAdded(@RequestParam("id") final Integer playlistId) {
        firePlaylistEvent(playlistId, EventType.PLAYLIST_ADDED);
    }

    @RequestMapping(value = "/playlist/deleted", method = RequestMethod.POST)
    public void playlistDeleted(@RequestParam("id") final Integer playlistId) {
        firePlaylistEvent(playlistId, EventType.PLAYLIST_DELETED);
    }

    private void fireTrackEvent(final Integer trackId, final Integer playlistId, final EventType type) {
        final User user = SecurityUtil.currentUser();
        if (user == null) {
            return;
        }
        final String trackName = trackService.get(trackId).getTitle();
        final String playlistName = playlistService.get(trackId).getName();

        publisher.publishEvent(new TrackEvent(trackId, playlistId, trackName, playlistName, user.getId(), type));
    }

    private void fireTrackEvent(final Integer trackId, final EventType type) {
        final User user = SecurityUtil.currentUser();
        if (user == null) {
            return;
        }
        final String trackName = trackService.get(trackId).getTitle();
        final String playlistName = playlistService.get(trackId).getName();

        publisher.publishEvent(new TrackEvent(trackId, null, trackName, null, user.getId(), type));
    }

    private void firePlaylistEvent(final Integer playlistId, final EventType type) {
        final User user = SecurityUtil.currentUser();
        if (user == null) {
            return;
        }
        final String playlistName = playlistService.get(playlistId).getName();
        publisher.publishEvent(new PlaylistEvent(playlistId, playlistName, user.getId(), type));
    }
}