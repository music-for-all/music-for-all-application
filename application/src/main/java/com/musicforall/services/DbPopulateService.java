package com.musicforall.services;

import com.musicforall.files.manager.FileManager;
import com.musicforall.history.service.DBHistoryPopulateService;
import com.musicforall.model.*;
import com.musicforall.model.user.User;
import com.musicforall.services.artist.ArtistService;
import com.musicforall.model.user.UserData;
import com.musicforall.services.follower.FollowerService;
import com.musicforall.services.playlist.PlaylistService;
import com.musicforall.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.io.FilenameUtils.getName;

/**
 * Populates the database with sample tracks, playlists, and users when the application starts up.
 */
@Component
public class DbPopulateService {
    private static final Logger LOG = LoggerFactory.getLogger(DbPopulateService.class);

    private static final Map<String, String> LINKS = new LinkedHashMap<>();

    private static final String OPEN_SOURCE_MUSIC_HOST = "http://opensourcemusic.com/files";

    private static final String USER_PICTURE_LINK = "https://developers.google.com/experts/img/user/user-default.png";

    private static final String DEFAULT_NAME = "Unknown";

    private static final String DEFAULT_BIO = "Jedi";

    private static final String USER_IS_SAVED = "user {} is saved";

    private static final String USER_IS_FOLLOW = "user {} is follow {}";

    static {
        /*
        LINKS.put("Jerry-Lee-Lewis-part-1", OPEN_SOURCE_MUSIC_HOST +
                "/2010/04/01-Selvin-On-The-City-Jerry-Lee-Lewis-part-1.mp3");
        LINKS.put("Tom-Waits-part-1", OPEN_SOURCE_MUSIC_HOST +
                "/2012/10/01-Tom-Waits-on-Selvin-On-The-City-part-1.mp3");
        LINKS.put("Jerry-Lee-Lewis-part-4", OPEN_SOURCE_MUSIC_HOST +
                "/2010/04/04-Selvin-On-The-City-Jerry-Lee-Lewis-part-4.mp3");
        */
        LINKS.put("Prelude 01", "http://www.mfiles.co.uk/mp3-downloads/book1-prelude01.mp3");
        LINKS.put("Prelude 02", "http://www.mfiles.co.uk/mp3-downloads/book1-prelude02.mp3");
        LINKS.put("Fugue 02", "http://www.mfiles.co.uk/mp3-downloads/book1-fugue02.mp3");
        LINKS.put("Prelude 03", "http://www.mfiles.co.uk/mp3-downloads/book1-prelude03.mp3");
        LINKS.put("Prelude 06", "http://www.mfiles.co.uk/mp3-downloads/book1-prelude06.mp3");
    }

    @Autowired
    private ExecutorService executorService;

    @Autowired
    private UserService userService;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private FollowerService followerService;

    @Autowired
    private FileManager fileManager;

    @Autowired
    private DBHistoryPopulateService dbHistoryPopulateService;

    private static URL toURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            LOG.error("URL is malformed {}", url, e);

        }
        return null;
    }

    private static long getFileSize(final Future<Path> future) {
        File[] files = new File[0];
        try {
            files = future.get().toFile().listFiles();
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("Future execution failed!", e);
        }
        if (files == null || files.length <= 0) {
            return 0L;
        }
        return Stream.of(files)
                .map(File::length)
                .reduce(0L, (x, y) -> x + y);
    }

    private static void setDefaultValues(User user, String username) {
        final boolean isPublicRadio = true;
        user.setUserData(new UserData(user, username, DEFAULT_NAME, DEFAULT_NAME,
                USER_PICTURE_LINK, DEFAULT_BIO, isPublicRadio));
    }

    @PostConstruct
    private void populate() {
        final boolean hasUsers = !userService.findAll().isEmpty();
        if (hasUsers) {
            return;
        }

        LOG.info("going to populate database with test data");

        final User user = new User("password", "dev@musicforall.com");
        setDefaultValues(user, "dev");
        userService.save(user);
        LOG.info(USER_IS_SAVED, user);

        final User user2 = new User("password2", "dev_C-3PO@musicforall.com");
        setDefaultValues(user2, "C-3PO");
        userService.save(user2);
        LOG.info(USER_IS_SAVED, user2);

        followerService.follow(user.getId(), user2.getId());
        LOG.info(USER_IS_FOLLOW, user, user2);
        followerService.follow(user2.getId(), user.getId());
        LOG.info(USER_IS_FOLLOW, user2, user);

        final User user3 = new User("password3", "dev_R2-D2@musicforall.com");
        setDefaultValues(user3, "R2-D2");
        userService.save(user3);
        LOG.info(USER_IS_SAVED, user3);

        followerService.follow(user.getId(), user3.getId());
        LOG.info(USER_IS_FOLLOW, user, user3);
        followerService.follow(user3.getId(), user2.getId());
        LOG.info(USER_IS_FOLLOW, user3, user2);

        final Set<Tag> tags = new HashSet<>(Arrays.asList(new Tag("Dummy"), new Tag("Classic"), new Tag("2016")));

        fileManager.clearDirectory();
        final List<Callable<Path>> tasks = LINKS.values().stream().map(DbPopulateService::toURL)
                .filter(u -> u != null)
                .peek(u -> LOG.info("going to saveTrack file by url - {}", u))
                .map(url -> (Callable<Path>) () -> fileManager.saveTrack(url).get())
                .collect(toList());

        try {
            final List<Future<Path>> futures = executorService.invokeAll(tasks);

            final List<Long> sizes = futures.stream()
                    .map(DbPopulateService::getFileSize)
                    .collect(toList());

            final Iterator<Long> iterator = sizes.iterator();

            final Artist artist = new Artist("Johann Sebastian Bach");
            artistService.save(artist);

            final List<Track> tracks = LINKS.entrySet().stream()
                    .map(entry -> {
                        final Track track = new Track(entry.getKey(), artist, "The Best",
                                getName(entry.getValue()), tags);
                        track.setSize(iterator.next());
                        return track;
                    })
                    .collect(toList());

            final Playlist playlist = new Playlist("Hype", new HashSet<>(tracks), user);

            for (Track track : tracks) {
                track.getPlaylists().add(playlist);
            }
            playlistService.save(playlist);

            final List<Integer> tracksId = tracks.stream().map(Track::getId).collect(toList());
            dbHistoryPopulateService.populateTrackListened(tracksId, user.getId());

            dbHistoryPopulateService.populateTrackLikedByFollowedUsers(tracksId, user2.getId());

            LOG.info("playlist {} is saved", playlist);

            LOG.info("finished database population");
        } catch (InterruptedException e) {
            LOG.error("Saving failed", e);
        }
    }
}
