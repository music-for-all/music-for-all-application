package com.musicforall.services;

import com.musicforall.files.manager.FileManager;
import com.musicforall.history.service.DBHistoryPopulateService;
import com.musicforall.model.Playlist;
import com.musicforall.model.Tag;
import com.musicforall.model.Track;
import com.musicforall.model.User;
import com.musicforall.history.handlers.HistoryEventListener;
import com.musicforall.history.handlers.events.TrackLikedEvent;
import com.musicforall.services.follower.FollowerService;
import com.musicforall.services.playlist.PlaylistService;
import com.musicforall.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.io.FilenameUtils.getName;

/**
 * Populates the database with sample tracks, playlists, and users when the application starts up.
 */
@Component
public class DbPopulateService {
    private static final Logger LOG = LoggerFactory.getLogger(DbPopulateService.class);

    private static final Map<String, String> LINKS = new HashMap<>();

    private static final String OPEN_SOURCE_MUSIC_HOST = "http://opensourcemusic.com/files";

    private static final String USER_IS_SAVED = "user {} is saved";

    private static final String USER_IS_FOLLOW = "user {} is follow {}";

    static {
        LINKS.put("Jerry-Lee-Lewis-part-1", OPEN_SOURCE_MUSIC_HOST +
                "/2010/04/01-Selvin-On-The-City-Jerry-Lee-Lewis-part-1.mp3");
        LINKS.put("Tom-Waits-part-1", OPEN_SOURCE_MUSIC_HOST +
                "/2012/10/01-Tom-Waits-on-Selvin-On-The-City-part-1.mp3");
        LINKS.put("Jerry-Lee-Lewis-part-4", OPEN_SOURCE_MUSIC_HOST +
                "/2010/04/04-Selvin-On-The-City-Jerry-Lee-Lewis-part-4.mp3");
    }

    @Autowired
    private ExecutorService executorService;

    @Autowired
    private UserService userService;

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private FollowerService followerService;

    @Autowired
    private FileManager fileManager;

    @Autowired
    private DBHistoryPopulateService dbHistoryPopulateService;

    @Autowired
    private HistoryEventListener historyEventListener;

    private static URL toURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            LOG.error("URL is malformed {}", url, e);
        }
        return null;
    }

    @PostConstruct
    private void populate() {
        final boolean hasUsers = !userService.findAll().isEmpty();
        if (hasUsers) {
            return;
        }

        LOG.info("going to populate database with test data");

        final User user = new User("dev", "password", "dev@musicforall.com");
        userService.save(user);
        LOG.info(USER_IS_SAVED, user);

        final User user2 = new User("user2", "password2", "user1@musicforall.com");
        userService.save(user2);
        LOG.info(USER_IS_SAVED, user2);

        followerService.follow(user.getId(), user2.getId());
        LOG.info(USER_IS_FOLLOW, user, user2);
        followerService.follow(user2.getId(), user.getId());
        LOG.info(USER_IS_FOLLOW, user2, user);

        final User user3 = new User("user3", "password3", "user2@musicforall.com");
        userService.save(user3);
        LOG.info(USER_IS_SAVED, user3);

        followerService.follow(user.getId(), user3.getId());
        LOG.info(USER_IS_FOLLOW, user, user3);
        followerService.follow(user3.getId(), user2.getId());
        LOG.info(USER_IS_FOLLOW, user3, user2);

        final Set<Tag> tags = new HashSet<>(Arrays.asList(new Tag("Dummy"), new Tag("Classic"), new Tag("2016")));

        final Set<Track> tracks = LINKS.entrySet().stream()
                .map(entry -> new Track(entry.getKey(), getName(entry.getValue()), tags))
                .collect(toSet());

        final Playlist playlist = new Playlist("Hype", tracks, user);

        playlistService.save(playlist);

        final List<Integer> tracksId = tracks.stream().map(Track::getId).collect(toList());
        dbHistoryPopulateService.populateTrackListened(tracksId, user.getId());

        LOG.info("playlist {} is saved", playlist);

        final List<Callable<Path>> tasks = LINKS.values().stream().map(DbPopulateService::toURL)
                .filter(u -> u != null)
                .peek(u -> LOG.info("going to save file by url - {}", u))
                .map(url -> (Callable<Path>) () -> fileManager.save(url))
                .collect(toList());

        final int FOLLOWED_USER_ID = 2;
        final int TRACK_1_ID = 1;
        final int TRACK_2_ID = 2;
        final int TRACK_3_ID = 3;

        historyEventListener.handleTrackLiked(new TrackLikedEvent(TRACK_1_ID, FOLLOWED_USER_ID));
        historyEventListener.handleTrackLiked(new TrackLikedEvent(TRACK_1_ID, FOLLOWED_USER_ID));
        historyEventListener.handleTrackLiked(new TrackLikedEvent(TRACK_3_ID, FOLLOWED_USER_ID));
        historyEventListener.handleTrackLiked(new TrackLikedEvent(TRACK_2_ID, FOLLOWED_USER_ID));
        historyEventListener.handleTrackLiked(new TrackLikedEvent(TRACK_2_ID, FOLLOWED_USER_ID));
        historyEventListener.handleTrackLiked(new TrackLikedEvent(TRACK_2_ID, FOLLOWED_USER_ID));

        try {
            executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            LOG.error("interrupted ", e);
        } finally {
            LOG.info("finished database population");
        }
    }
}
