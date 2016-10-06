package com.musicforall.history.handlers.events;

public class TrackEvent extends Event {

    private final int trackId;
    private final String trackName;
    private final Integer playlistId;
    private final String playlistName;

    public TrackEvent(int trackId, Integer playlistId, String trackName, String playlistName,
                      int userId, EventType type) {
        super(userId, type);
        this.trackId = trackId;
        this.trackName = trackName;
        this.playlistId = playlistId;
        this.playlistName = playlistName;
    }

    public int getTrackId() {
        return trackId;
    }

    public String getTrackName() {
        return trackName;
    }

    public Integer getPlaylistId() {
        return playlistId;
    }

    public String getPlaylistName() {
        return playlistName;
    }
}
