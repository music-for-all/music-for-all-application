package com.musicforall.model;

import javax.validation.constraints.Size;
import java.util.List;

public class SearchTrackRequest {

    @Size(max = 16)
    private String trackName;

    private String artistName;

    @Size(max = 16)
    private String album;

    private List<String> tags;

    public SearchTrackRequest() {
    }

    public SearchTrackRequest(String trackName, String artistName, String album, List<String> tags) {
        this.trackName = trackName;
        this.artistName = artistName;
        this.album = album;
        this.tags = tags;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
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
                ", artistName='" + artistName + '\'' +
                ", album='" + album + '\'' +
                ", tags=" + tags +
                '}';
    }
}