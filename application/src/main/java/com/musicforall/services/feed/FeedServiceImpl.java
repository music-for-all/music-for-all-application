package com.musicforall.services.feed;

import com.musicforall.history.model.History;
import com.musicforall.history.service.history.HistoryService;
import com.musicforall.model.Playlist;
import com.musicforall.model.Track;
import com.musicforall.model.User;
import com.musicforall.services.follower.FollowerService;
import com.musicforall.services.playlist.PlaylistService;
import com.musicforall.services.track.TrackService;
import com.musicforall.services.user.UserService;
import com.musicforall.util.FeedUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    public Map<User, List<Feed>> getGroupedFollowingFeeds(Integer userId) {
        final Collection<Integer> usersIds = followerService.getFollowingId(userId);
        final List<User> users = userService.getUsersById(usersIds);
        List<Integer> track_ids = new ArrayList<>();
        List<Integer> playlist_ids = new ArrayList<>();

        final Collection<History> usersHistories = historyService.getUsersHistories(usersIds);

        usersHistories
                .stream()
                .forEach(
                        h -> {
                            if (h.getEventType().isTrackEvent()) {
                                track_ids.add(h.getTrackId());
                            } else if (h.getEventType().isPlaylistEvent()) {
                                playlist_ids.add(h.getPlaylistId());
                            }
                        }
                );
        List<Track> tracks = new ArrayList<>(trackService.getAllById(track_ids));
        List<Playlist> playlists = new ArrayList<>(playlistService.getAllById(playlist_ids));

        return usersHistories
                .stream()
                .collect(Collectors.groupingBy(h -> users
                                .stream()
                                .filter(u -> u.getId().equals(h.getUserId()))
                                .findFirst()
                                .get(),
                        LinkedHashMap::new,
                        Collectors.mapping(h -> {
                            if (h.getEventType().isTrackEvent()) {
                                return new Feed(h.getEventType(),
                                        FeedUtil.getFilteredTrack(tracks, h).getEntireName(), h.getDate());
                            } else if (h.getEventType().isPlaylistEvent()) {
                                return new Feed(h.getEventType(),
                                        FeedUtil.getFilteredPlaylist(playlists, h).getName(), h.getDate());
                            }
                            return null;
                        }
                                , Collectors.toList())));
    }
}

