package com.musicforall.web.history;

import com.musicforall.history.handlers.events.EventType;
import com.musicforall.history.handlers.events.PlaylistEvent;
import com.musicforall.history.handlers.events.TrackEvent;
import com.musicforall.model.Playlist;
import com.musicforall.model.Track;
import com.musicforall.services.achievements.AchievementProcessor;
import com.musicforall.services.playlist.PlaylistService;
import com.musicforall.services.track.TrackService;
import com.musicforall.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

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
    @Autowired
    private AchievementProcessor achievementProcessor;

    @RequestMapping(value = "/types", method = GET)
    public EventType[] eventTypes() {
        return EventType.values();
    }

    @RequestMapping(value = "/track/liked", method = POST)
    public void trackLiked(@RequestParam("trackId") final Integer trackId) {
        fireTrackEvent(trackId, null, EventType.TRACK_LIKED);
    }

    @RequestMapping(value = "/track/listened", method = POST)
    public void trackListened(@RequestParam("trackId") final Integer trackId) {
        fireTrackEvent(trackId, null, EventType.TRACK_LISTENED);
    }

    @RequestMapping(value = "/track/added", method = POST)
    public void trackAdded(@RequestParam("trackId") final Integer trackId,
                           @RequestParam("playlistId") final Integer playlistId) {
        fireTrackEvent(trackId, playlistId, EventType.TRACK_ADDED);
    }

    @RequestMapping(value = "/track/deleted", method = POST)
    public void trackDeleted(@RequestParam("trackId") final Integer trackId,
                             @RequestParam("playlistId") final Integer playlistId) {
        fireTrackEvent(trackId, playlistId, EventType.TRACK_DELETED);
    }

    @RequestMapping(value = "/playlist/added", method = POST)
    public void playlistAdded(@RequestParam("playlistId") final Integer playlistId) {
        firePlaylistEvent(playlistId, EventType.PLAYLIST_ADDED);
    }

    @RequestMapping(value = "/playlist/deleted", method = POST)
    public void playlistDeleted(@RequestParam("playlistId") final Integer playlistId) {
        firePlaylistEvent(playlistId, EventType.PLAYLIST_DELETED);
    }

    private void fireTrackEvent(final Integer trackId, final Integer playlistId, final EventType type) {
        final Integer userId = SecurityUtil.currentUserId();
        if (userId == null) {
            return;
        }
        final Track track = trackService.get(trackId);
        final Playlist playlist = playlistId != null ? playlistService.get(playlistId) : null;

        publisher.publishEvent(new TrackEvent(trackId, playlistId, track.getName(),
                playlist != null ? playlist.getName() : null, userId, type));

        achievementProcessor.process(track, userId, type);
    }

    private void firePlaylistEvent(final Integer playlistId, final EventType type) {
        final Integer userId = SecurityUtil.currentUserId();
        if (userId == null) {
            return;
        }
        final Playlist playlist = playlistService.get(playlistId);
        publisher.publishEvent(new PlaylistEvent(playlistId, playlist.getName(), userId, type));

        achievementProcessor.process(playlist, userId, type);
    }
}