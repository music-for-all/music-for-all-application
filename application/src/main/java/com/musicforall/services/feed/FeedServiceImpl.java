package com.musicforall.services.feed;

import com.musicforall.dto.feed.Feed;
import com.musicforall.dto.userFeedsDTO.UserFeedsDTO;
import com.musicforall.history.handlers.events.EventType;
import com.musicforall.history.model.History;
import com.musicforall.history.service.history.HistoryService;
import com.musicforall.model.Artist;
import com.musicforall.model.Track;
import com.musicforall.model.user.User;
import com.musicforall.services.follower.FollowerService;
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

    private static final String TRACKNAME_FORMAT = "<span class=\"track-name\">{0} - {1}</span>";
    private static final String PLAYLISTNAME_FORMAT = "<span class=\"playlist-name\">{0}</span>";
    private static final int NINE = 9;

    @Autowired
    private FollowerService followerService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private UserService userService;

    @Autowired
    private TrackService trackService;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;


    @Override
    public List<UserFeedsDTO> getGroupedFollowingFeeds(Integer userId) {
        final Collection<Integer> usersIds = followerService.getFollowingId(userId);
        final Collection<History> usersHistories = historyService.getUsersHistories(usersIds);
        final List<Integer> tracksIds = getTracksIds(usersHistories);

        final Map<Integer, User> usersByIds = getUsersByIds(usersIds);
        final Map<Integer, Track> tracksByIds = getTracksByIds(tracksIds);

        final Map<User, Collection<Feed>> feedsByUser =
                getFeedsFromHistories(usersHistories, usersByIds, tracksByIds);

        return getUserFeedsDTO(feedsByUser);
    }

    private List<Integer> getTracksIds(Collection<History> usersHistories) {
        return usersHistories.stream()
                .filter(h -> h.getEventType().isTrackEvent())
                .map(History::getTrackId)
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

    private Map<User, Collection<Feed>> getFeedsFromHistories(Collection<History> histories,
                                                              Map<Integer, User> usersByIds,
                                                              Map<Integer, Track> tracksByIds) {
        return histories
                .stream()
                .collect(Collectors.groupingBy(
                        h -> usersByIds.get(h.getUserId()),
                        LinkedHashMap::new,
                        Collectors.mapping(h -> {
                                    if (h.getEventType().isTrackEvent()) {
                                        return generateContent(
                                                h.getEventType(),
                                                h.getDate(),
                                                Arrays.asList(
                                                        getFormattedTrack(h, tracksByIds),
                                                        getFormattedPlaylist(h)));
                                    } else if (h.getEventType().isPlaylistEvent()) {
                                        return generateContent(
                                                h.getEventType(),
                                                h.getDate(),
                                                Arrays.asList(getFormattedPlaylist(h)));
                                    }
                                    return null;
                                },
                                Collectors.toCollection(ArrayList::new))));
    }

    private List<UserFeedsDTO> getUserFeedsDTO(Map<User, Collection<Feed>> feedsByUser) {
        return feedsByUser.entrySet().stream()
                .map(e -> new UserFeedsDTO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    private String getFormattedTrack(History history, Map<Integer, Track> tracksByIds) {
        Track track = tracksByIds.get(history.getTrackId());
        final Artist artist = track.getArtist();
        if (artist == null) {
            return MessageFormat.format(TRACKNAME_FORMAT,
                    messageSource.getMessage("followingpage.unknown", null, LocaleContextHolder.getLocale()),
                    track.getName());
        }
        return MessageFormat.format(TRACKNAME_FORMAT, artist.getName(), track.getName());
    }

    private String getFormattedPlaylist(History history) {
        return MessageFormat.format(PLAYLISTNAME_FORMAT, history.getPlaylistName());
    }

    private Feed generateContent(EventType eventType, Date date, List<String> params) {
        switch (eventType) {
            case TRACK_LISTENED:
                return new Feed(
                        messageSource.getMessage("followingpage.listenedTrack", params.toArray(),
                                LocaleContextHolder.getLocale()), formatDate(date));
            case TRACK_LIKED:
                return new Feed(
                        messageSource.getMessage("followingpage.likedTrack", params.toArray(),
                                LocaleContextHolder.getLocale()), formatDate(date));
            case TRACK_ADDED:
                return new Feed(
                        messageSource.getMessage("followingpage.addedTrack", params.toArray(),
                                LocaleContextHolder.getLocale()), formatDate(date));
            case TRACK_DELETED:
                return new Feed(
                        messageSource.getMessage("followingpage.deletedTrack", params.toArray(),
                                LocaleContextHolder.getLocale()), formatDate(date));
            case PLAYLIST_ADDED:
                return new Feed(
                        messageSource.getMessage("followingpage.addedPlaylist", params.toArray(),
                                LocaleContextHolder.getLocale()), formatDate(date));
            case PLAYLIST_DELETED:
                return new Feed(
                        messageSource.getMessage("followingpage.deletedPlaylist", params.toArray(),
                                LocaleContextHolder.getLocale()), formatDate(date));
            default:
                return null;
        }
    }

    private String formatDate(Date date) {
        final Calendar feedDate = new GregorianCalendar();
        feedDate.setTime(date);
        final Calendar today = new GregorianCalendar();

        final int hours = feedDate.get(Calendar.HOUR_OF_DAY);
        final int minutes = feedDate.get(Calendar.MINUTE);
        StringBuilder formattedDate = new StringBuilder();
        String day = today.after(feedDate) ? "Yesterday" : "Today";
        formattedDate.append(day).append(" ");
        formattedDate.append(hours > NINE ? Integer.toString(hours) : "0" + hours).append(":");
        formattedDate.append(minutes > NINE ? Integer.toString(minutes) : "0" + minutes);

        return formattedDate.toString();
    }
}

