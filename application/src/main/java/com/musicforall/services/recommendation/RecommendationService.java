package com.musicforall.services.recommendation;

import com.musicforall.common.dao.Dao;
import com.musicforall.common.dao.QueryParams;
import com.musicforall.history.handlers.events.EventType;
import com.musicforall.history.model.History;
import com.musicforall.history.service.history.HistoryService;
import com.musicforall.model.Playlist;
import com.musicforall.model.Tag;
import com.musicforall.model.Track;
import com.musicforall.model.User;
import com.musicforall.services.follower.FollowerService;
import com.musicforall.services.playlist.PlaylistService;
import com.musicforall.services.track.TrackService;
import com.musicforall.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by kgavrylchenko on 16.08.16.
 */
@Component
@Transactional
public class RecommendationService {

    @Autowired
    private Dao dao;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private TrackService trackService;

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private FollowerService followerService;

    public Collection<Track> getMostPopularTracks() {
        final List<Integer> popularTracksIds = historyService.getTheMostPopularTracks();
        return trackService.getAllById(popularTracksIds);
    }

    public Collection<Tag> getPopularTags() {
        final List<Integer> ids = historyService.getTheMostPopularTracks();

        final int count = 20;
        final int offset = 0;

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("ids", ids);
        final Collection<Tag> tags = dao.getAllByNamedQuery(Tag.class, Tag.POPULAR_TAGS_BY_TRACK_ID_QUERY,
                parameters, new QueryParams(count, offset));
        return tags;
    }

    public Collection<Track> getRecommendedTracks() {
        final User user = SecurityUtil.currentUser();

        final Collection<Integer> ids = followerService.getFollowingId(user.getId());
        final Collection<History> histories = historyService.getAllForUsers(EventType.TRACK_LIKED, ids);
        final Collection<Integer> userTracks = usersTracksIds(user);

        final Map<Integer, Integer> likesCountsByTrackId = histories.stream()
                .map(History::getTrackId)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Function.identity(), i -> 1, Integer::sum));

        final List<Integer> tracksOrderedByLikes = likesCountsByTrackId
                .keySet().stream()
                .filter(k -> !userTracks.contains(k))
                .sorted((k1, k2) -> likesCountsByTrackId.get(k1).compareTo(likesCountsByTrackId.get(k2)))
                .collect(Collectors.toList());

        return trackService.getAllById(tracksOrderedByLikes);
    }

    private List<Integer> usersTracksIds(User user) {
        final Set<Playlist> allUserPlaylist = playlistService.getAllUserPlaylists(user.getId());
        return allUserPlaylist.stream()
                .flatMap(p -> p.getTracks().stream())
                .map(Track::getId)
                .distinct()
                .collect(Collectors.toList());
    }
}
