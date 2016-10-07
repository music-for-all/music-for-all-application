package com.musicforall.web.history;

import com.musicforall.history.handlers.events.EventType;
import com.musicforall.history.handlers.events.PlaylistEvent;
import com.musicforall.history.handlers.events.TrackEvent;
import com.musicforall.model.user.User;
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

    @RequestMapping(value = "/track/liked", method = RequestMethod.POST)
    public void trackLiked(@RequestParam("id") final Integer trackId) {
        fireTrackEvent(trackId, EventType.TRACK_LIKED);
    }

    @RequestMapping(value = "/track/listened", method = RequestMethod.POST)
    public void trackListened(@RequestParam("id") final Integer trackId) {
        fireTrackEvent(trackId, EventType.TRACK_LISTENED);
    }

    @RequestMapping(value = "/track/added", method = RequestMethod.POST)
    public void trackAdded(@RequestParam("id") final Integer trackId) {
        fireTrackEvent(trackId, EventType.TRACK_ADDED);
    }

    @RequestMapping(value = "/track/deleted", method = RequestMethod.POST)
    public void trackDeleted(@RequestParam("id") final Integer trackId) {
        fireTrackEvent(trackId, EventType.TRACK_DELETED);
    }

    @RequestMapping(value = "/playlist/added", method = RequestMethod.POST)
    public void playlistAdded(@RequestParam("id") final Integer playlistId) {
        firePlaylistEvent(playlistId, EventType.PLAYLIST_ADDED);
    }

    @RequestMapping(value = "/playlist/deleted", method = RequestMethod.POST)
    public void playlistDeleted(@RequestParam("id") final Integer playlistId) {
        firePlaylistEvent(playlistId, EventType.PLAYLIST_DELETED);
    }

    private void fireTrackEvent(final Integer trackId, final EventType type) {
        final User user = SecurityUtil.currentUser();
        if (user == null) {
            return;
        }
        publisher.publishEvent(new TrackEvent(trackId, user.getId(), type));
    }

    private void firePlaylistEvent(final Integer playlistId, final EventType type) {
        final User user = SecurityUtil.currentUser();
        if (user == null) {
            return;
        }
        publisher.publishEvent(new PlaylistEvent(playlistId, user.getId(), type));
    }
}