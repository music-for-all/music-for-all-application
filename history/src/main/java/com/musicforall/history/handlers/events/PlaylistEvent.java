package com.musicforall.history.handlers.events;

/**
 * @author ENikolskiy.
 */
public class PlaylistEvent extends Event {
    private final int playlistId;

    public PlaylistEvent(int playlistId, int userId, EventType type) {
        super(userId, type);
        this.playlistId = playlistId;
    }

    public int getPlaylistId() {
        return playlistId;
    }
}
