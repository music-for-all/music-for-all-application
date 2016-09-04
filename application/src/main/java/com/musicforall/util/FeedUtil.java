package com.musicforall.util;

import com.musicforall.history.model.History;
import com.musicforall.model.Playlist;
import com.musicforall.model.Track;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author IliaNik on 04.09.2016.
 */
public final class FeedUtil {

    private static final Logger LOG = LoggerFactory.getLogger(FeedUtil.class);

    private FeedUtil() {
    }

    public static Track getFilteredTrack(List<Track> tracks, History history) {
        return tracks
                .stream()
                .filter(t -> t.getId().equals(history.getTrackId()))
                .findFirst()
                .get();
    }

    public static Playlist getFilteredPlaylist(List<Playlist> playlists, History history) {
        return playlists
                .stream()
                .filter(p -> p.getId().equals(history.getPlaylistId()))
                .findFirst()
                .get();
    }

}
