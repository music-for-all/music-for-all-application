package com.musicforall.services;

import com.musicforall.files.manager.FileManager;
import com.musicforall.model.Playlist;
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
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.io.FilenameUtils.getName;

@Component
public class DbPopulateService {
    private static final Logger LOG = LoggerFactory.getLogger(DbPopulateService.class);

    private final String[] links = {
            "http://cdndl.zaycev.net/46015/2158629/garbage_-_cherry_lips_(zaycev.net).mp3",
            "http://cdndl.zaycev.net/151375/2982474/kendrick_lamar_-_m.a.a.d._city_eprom_remix_(zaycev.net).mp3",
            "http://cdndl.zaycev.net/151375/1782090/kendrick_lamar_-_swimming_pool_(zaycev.net).mp3",
            "http://cdndl.zaycev.net/38302/3956198/drake_-_hotling_bling_(zaycev.net).mp3"
    };

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

        final User user = new User("dev", "password", "dev@musicforall.com");
        userService.save(user);

        final Set<Track> tracks = Arrays.stream(links)
                .map(link -> new Track(parseTrackLink(link)[1], getName(link)))
                .collect(Collectors.toSet());

        final Playlist playlist = new Playlist("Hype", tracks, user);
        playlistService.save(playlist);

        Arrays.stream(links).map(DbPopulateService::toURL)
                .filter(l -> l != null)
                .forEach(fileManager::save);

    }

    private String[] parseTrackLink(final String link) {
        return getName(link).replaceAll("_", " ").replace("(zaycev.net).mp3", "").trim().split(" - ");
    }
}
