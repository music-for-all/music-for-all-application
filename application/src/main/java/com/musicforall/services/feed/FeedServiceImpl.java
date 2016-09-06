package com.musicforall.services.feed;

import com.musicforall.dto.feed.Feed;
import com.musicforall.history.handlers.events.EventType;
import com.musicforall.history.model.History;
import com.musicforall.history.service.history.HistoryService;
import com.musicforall.model.Playlist;
import com.musicforall.model.Track;
import com.musicforall.model.User;
import com.musicforall.services.follower.FollowerService;
import com.musicforall.services.playlist.PlaylistService;
import com.musicforall.services.track.TrackService;
import com.musicforall.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author IliaNik on 02.09.2016.
 */

@Service("feedService")
@Transactional
public class FeedServiceImpl implements FeedService {

    @Autowired
    private FollowerService followerService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private UserService userService;

    @Autowired
    private TrackService trackService;

    @Autowired
    private PlaylistService playlistService;


    @Override
    public Map<User, Collection<String>> getGroupedFollowingFeeds(Integer userId) {
        final Collection<Integer> usersIds = followerService.getFollowingId(userId);

        Map<Integer, User> usersByIds = userService.getUsersById(usersIds).stream().collect(Collectors.toMap(User::getId, Function.identity()));

        final Collection<History> usersHistories = historyService.getUsersHistories(usersIds);


        final List<Integer> tracksIds = usersHistories.stream()
                .filter(h -> h.getEventType().isTrackEvent())
                .map(History::getTrackId)
                .collect(Collectors.toList());

        final List<Integer> playlistsIds = usersHistories.stream()
                .filter(h -> h.getEventType().isPlaylistEvent())
                .map(History::getPlaylistId)
                .collect(Collectors.toList());

        Map<Integer, Track> tracksByIds = trackService.getAllByIds(tracksIds).stream().collect(Collectors.toMap(Track::getId, Function.identity()));
        Map<Integer, Playlist> playlistsByIds = playlistService.getAllByIds(playlistsIds).stream().collect(Collectors.toMap(Playlist::getId, Function.identity()));

        return usersHistories
                .stream()
                .collect(Collectors.groupingBy(h -> usersByIds.get(h.getUserId()),
                        LinkedHashMap::new,
                        Collectors.mapping(h -> {
                            if (h.getEventType().isTrackEvent()) {
                                return generateContent(h.getEventType(),
                                        tracksByIds.get(h.getTrackId()).getEntireName(), h.getDate());
                            } else if (h.getEventType().isPlaylistEvent()) {
                                return generateContent(h.getEventType(),
                                        playlistsByIds.get(h.getTrackId()).getName(), h.getDate());
                            }
                            return null;
                        }, Collectors.toList())));

    }

    private Feed generateContent(EventType eventType, String target, Date date) {
        switch (eventType) {
            case TRACK_LISTENED:
                return new Feed("Listened the track " + target, date);
            case TRACK_LIKED:
                return new Feed("Liked the track " + target, date);
            case TRACK_ADDED:
                return new Feed("Added the track " + target, date);
            case TRACK_DELETED:
                return new Feed("Deleted the track " + target, date);
            case PLAYLIST_ADDED:
                return new Feed("Added the playlist " + target, date);
            case PLAYLIST_DELETED:
                return new Feed("Deleted the playlist " + target, date);
            default:
                return null;
        }
    }
}

