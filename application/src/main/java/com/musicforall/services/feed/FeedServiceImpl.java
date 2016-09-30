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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author IliaNik on 02.09.2016.
 */

@Service("feedService")
@Transactional
public class FeedServiceImpl implements FeedService {

    static final String TRACKNAME_FORMAT = "{0} - {1}";

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

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;


    @Override
    public Map<User, Collection<Feed>> getGroupedFollowingFeeds(Integer userId) {
        final Collection<Integer> usersIds = followerService.getFollowingId(userId);
        final Collection<History> usersHistories = historyService.getUsersHistories(usersIds);
        final List<Integer> tracksIds = getTrackIds(usersHistories);
        final List<Integer> playlistsIds = getPlaylistIds(usersHistories);

        final Map<Integer, User> usersByIds = getUsersByIds(usersIds);
        final Map<Integer, Track> tracksByIds = getTracksByIds(tracksIds);
        final Map<Integer, Playlist> playlistsByIds = getPlaylistsByIds(playlistsIds);

        return getFeedsFromHistories(usersHistories, usersByIds, tracksByIds, playlistsByIds);
    }

    private List<Integer> getTrackIds(Collection<History> usersHistories) {
        return usersHistories.stream()
                .filter(h -> h.getEventType().isTrackEvent())
                .map(History::getTrackId)
                .collect(Collectors.toList());
    }

    private List<Integer> getPlaylistIds(Collection<History> usersHistories) {
        return usersHistories.stream()
                .filter(h -> h.getEventType().isPlaylistEvent())
                .map(History::getPlaylistId)
                .collect(Collectors.toList());
    }

    private Map<Integer, User> getUsersByIds(Collection<Integer> usersIds) {
        return userService.getUsersById(usersIds)
                .stream().collect(Collectors.toMap(User::getId, Function.identity()));
    }

    private Map<Integer, Track> getTracksByIds(List<Integer> tracksIds) {
        return trackService.getAllByIds(tracksIds)
                .stream().collect(Collectors.toMap(Track::getId, Function.identity()));
    }

    private Map<Integer, Playlist> getPlaylistsByIds(List<Integer> playlistsIds) {
        return playlistService.getAllByIds(playlistsIds)
                .stream().collect(Collectors.toMap(Playlist::getId, Function.identity()));
    }

    private Map<User, Collection<Feed>> getFeedsFromHistories(Collection<History> histories,
                                                              Map<Integer, User> usersByIds,
                                                              Map<Integer, Track> tracksByIds,
                                                              Map<Integer, Playlist> playlistsByIds) {
        return histories
                .stream()
                .collect(Collectors.groupingBy(h -> usersByIds.get(h.getUserId()),
                        LinkedHashMap::new,
                        Collectors.mapping(h -> {
                            if (h.getEventType().isTrackEvent()) {
                                return generateContent(h.getEventType(),
                                        MessageFormat.format(TRACKNAME_FORMAT,
                                                tracksByIds.get(h.getTrackId()).getArtist().getArtistName(),
                                                tracksByIds.get(h.getTrackId()).getTitle()),
                                        h.getDate());
                            } else if (h.getEventType().isPlaylistEvent()) {
                                return generateContent(h.getEventType(),
                                        playlistsByIds.get(h.getPlaylistId()).getName(),
                                        h.getDate());
                            }
                            return null;
                        }, Collectors.toCollection(ArrayList::new))));
    }

    private Feed generateContent(EventType eventType, String target, Date date) {
        Object[] params = new Object[]{target};
        switch (eventType) {
            case TRACK_LISTENED:
                return new Feed(
                        messageSource.getMessage("followingpage.listenedTrack", params,
                                LocaleContextHolder.getLocale()), date);
            case TRACK_LIKED:
                return new Feed(
                        messageSource.getMessage("followingpage.likedTrack", params,
                                LocaleContextHolder.getLocale()), date);
            case TRACK_ADDED:
                return new Feed(
                        messageSource.getMessage("followingpage.addedTrack", params,
                                LocaleContextHolder.getLocale()), date);
            case TRACK_DELETED:
                return new Feed(
                        messageSource.getMessage("followingpage.deletedTrack", params,
                                LocaleContextHolder.getLocale()), date);
            case PLAYLIST_ADDED:
                return new Feed(
                        messageSource.getMessage("followingpage.addedPlaylist", params,
                                LocaleContextHolder.getLocale()), date);
            case PLAYLIST_DELETED:
                return new Feed(
                        messageSource.getMessage("followingpage.deletedPlaylist", params,
                                LocaleContextHolder.getLocale()), date);
            default:
                return null;
        }
    }
}

