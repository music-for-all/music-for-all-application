package com.musicforall.services.recommendation;

import com.musicforall.history.handlers.events.EventType;
import com.musicforall.history.model.History;
import com.musicforall.history.service.history.HistoryService;
import com.musicforall.model.Playlist;
import com.musicforall.model.Track;
import com.musicforall.model.User;
import com.musicforall.services.follower.FollowerService;
import com.musicforall.services.playlist.PlaylistService;
import com.musicforall.services.track.TrackService;
import com.musicforall.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by kgavrylchenko on 16.08.16.
 */
@Component
public class RecommendationService {

    @Autowired
    private HistoryService historyService;
    @Autowired
    private TrackService trackService;
    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private FollowerService followerService;

    public Collection<Track> getMostPopularTracks() {
        List<Integer> popularTracksIds = historyService.getTheMostPopularTracks();
        return trackService.getAllById(popularTracksIds);
    }

    public Collection<Track> getRecommendedTracks() {
        final User user = SecurityUtil.currentUser();

        Collection<History> histories = historyService.getAllForUsers(EventType.TRACK_LIKED, followerService.getFollowingsIds(user.getId()));
        Collection<Integer> userTracks = usersTracksIds(user);

        Map<Integer, Integer> likesCountsByTrackId = histories.stream()
                .map(History::getTrackId)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Function.identity(), i -> 1, Integer::sum));

        List<Integer> tracksOrderedByLikes = likesCountsByTrackId
                .keySet().stream()
                .filter(k -> !userTracks.contains(k))
                .sorted((k1, k2) -> likesCountsByTrackId.get(k1).compareTo(likesCountsByTrackId.get(k2)))
                .collect(Collectors.toList());

        return trackService.getAllById(tracksOrderedByLikes);
    }

    private List<Integer> usersTracksIds(User user) {
        Set<Playlist> allUserPlaylist = playlistService.getAllUserPlaylists(user.getId());
        return allUserPlaylist.stream()
                .flatMap(p -> p.getTracks().stream())
                .map(Track::getId)
                .distinct()
                .collect(Collectors.toList());
    }
}
