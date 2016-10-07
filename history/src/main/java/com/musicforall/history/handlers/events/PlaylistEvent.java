package com.musicforall.history.handlers.events;

/**
 * @author ENikolskiy.
 */
public class PlaylistEvent extends Event {
    private final Integer playlistId;
    private final String playlistName;

    public PlaylistEvent(Integer playlistId, String playlistName, int userId, EventType type) {
        super(userId, type);
        this.playlistId = playlistId;
        this.playlistName = playlistName;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public String getPlaylistName() {
        return playlistName;
    }
}
