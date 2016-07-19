package com.musicforall.services;

import com.musicforall.files.manager.FileManager;
import com.musicforall.model.Playlist;
import com.musicforall.model.Tag;
import com.musicforall.model.Track;
import com.musicforall.model.User;
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

@Component
public class DbPopulateService {
    private static final Logger LOG = LoggerFactory.getLogger(DbPopulateService.class);

    private static final List<String> LINKS = new ArrayList<>();

    static {
        LINKS.add("http://dl.last.fm/static/1468934504/131564291/" +
                "16465bd6d0a968d21008d9f3618e657c5b8a71969f33040eeb66287b6eef1a5a/Best+Coast+-+The+Only+Place.mp3");
        LINKS.add("http://dl.last.fm/static/1468934504/134306392/" +
                "c15e941ca23aa69313dc1f2285af995ccfd00ef6c613e3fe2fadab64b7247e4f/Nils+Frahm+-+You.mp3");
        LINKS.add("http://dl.last.fm/static/1468934504/122620941/" +
                "9bbd58da2e510d432156315dad2b68ce56a992f13c81f600029b150e42fbad1f/Com+Truise+-+Cyanide+Sisters.mp3");
        LINKS.add("http://dl.last.fm/static/1468935011/125708103/" +
                "d6b7099525cb66b3891dc560786f11a18b34c84d13f30f8b3d3f428c8ca0598d/Starfucker+-+Bury+Us+Alive.mp3");
    }

    @Autowired
    private ExecutorService executorService;

    @Autowired
    private UserService userService;

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private FileManager fileManager;

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

        LOG.info("user {} is saved", user);

        final Set<Tag> tags = new HashSet<>(Arrays.asList(new Tag("Dummy"), new Tag("Classic"), new Tag("2016")));

        final Set<Track> tracks = LINKS.stream()
                .map(link -> new Track(tags, trackName(link), getName(link)))
                .collect(toSet());

        final Playlist playlist = new Playlist("Hype", tracks, user);
        playlistService.save(playlist);

        LOG.info("playlist {} is saved", playlist);

        List<Callable<Path>> tasks = LINKS.stream().map(DbPopulateService::toURL)
                .filter(u -> u != null)
                .peek(u -> LOG.info("going to save file by url - {}", u))
                .map(url -> (Callable<Path>) () -> fileManager.save(url))
                .collect(toList());
        try {
            executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            LOG.error("interrupted ", e);
        } finally {
            LOG.info("finished database population");
        }
    }

    private String trackName(final String link) {
        return getName(link).replace("+", " ").trim().split(" - ")[1];
    }
}
