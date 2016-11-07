package com.musicforall.history.handlers.events;

public class TrackEvent extends Event {

    private final Integer trackId;
    private final String trackName;
    private final String artistName;
    private final Integer playlistId;

    public TrackEvent(Integer trackId, String trackName,
                      Integer userId, EventType type, String artistName, Integer playlistId) {
        super(userId, type);
        this.trackId = trackId;
        this.trackName = trackName;
        this.artistName = artistName;
        this.playlistId = playlistId;
    }

    public int getTrackId() {
        return trackId;
    }

    public String getTrackName() {
        return trackName;
    }

    public String getArtistName() {
        return artistName;
    }

    public Integer getPlaylistId() {
        return playlistId;
    }
}
