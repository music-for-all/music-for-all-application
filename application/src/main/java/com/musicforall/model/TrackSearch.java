package com.musicforall.model;

import javax.validation.constraints.Size;
import java.util.List;

public class TrackSearch {

    @Size(min = 2, max = 16)
    private String title;

    @Size(min = 2, max = 16)
    private String artist;

    @Size(min = 2, max = 16)
    private String album;

    @Size(min = 2, max = 16)
    private List<String> tags;

    public TrackSearch() {
    }

    public TrackSearch(String title, String artist, String album, List<String> tags) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public List<String> getTags() {
        return tags;
    }
}