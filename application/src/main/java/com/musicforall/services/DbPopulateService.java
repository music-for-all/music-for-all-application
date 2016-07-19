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
        LINKS.add("http://dl.last.fm/static/1468876051/131211148" +
                "/a3c35916e23dcb5dafd20667e4015eeaa38460c16c45365ad2bcd0098b1266f1/Death+Grips+-+Get+Got.mp3");
        LINKS.add("http://dl.last.fm/static/1468876051" +
                "/126178029/2a3dc38084d8fc698a86888b314a146c25057eb383d639a4d5f5116615ec7935" +
                "/Death+Grips+-+Guillotine.mp3");
        LINKS.add("http://dl.last.fm/static/1468876051/133527789/" +
                "76ccb5716e7e9efb54ef1efab70fff2e0aea05e2d0b4866def49bc1167db240a/Death+Grips+-+No+Love.mp3");
        LINKS.add("http://dl.last.fm/static/1468876051/131211149/" +
                "ce5e43359f661b8abdb7d42e406379eca867a90214312b770ddab3ebb2b94adb/Death+Grips+-+Lost+Boys.mp3");
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

        List<Callable<Path>> aaa = LINKS.stream().map(DbPopulateService::toURL)
                .filter(u -> u != null)
                .peek(u -> LOG.info("going to save file by url - {}", u))
                .map(url -> (Callable<Path>) () -> fileManager.save(url))
                .collect(toList());
        try {
            executorService.invokeAll(aaa);
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
