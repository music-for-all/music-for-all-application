package com.musicforall.model;

import javax.validation.constraints.Size;
import java.util.List;

public class SearchTrackRequest {

    @Size(max = 16)
    private String trackName;

    private Artist artist;

    @Size(max = 16)
    private String album;

    private List<String> tags;

    public SearchTrackRequest() {
    }

    public SearchTrackRequest(String trackName, Artist artist, String album, List<String> tags) {
        this.trackName = trackName;
        this.artist = artist;
        this.album = album;
        this.tags = tags;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "SearchTrackRequest{" +
                "trackName='" + trackName + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", tags=" + tags +
                '}';
    }
}