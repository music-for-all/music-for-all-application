package com.musicforall.model;

import javax.validation.constraints.Size;

public class TrackSearch {

    @Size(min = 2, max = 16)
    private String title;

    @Size(min = 2, max = 16)
    private String artist;

    @Size(min = 2, max = 16)
    private String album;

    @Size(min = 2, max = 16)
    private String tag;

    public TrackSearch() {
    }

    public TrackSearch(String title, String artist, String album, String tag) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.tag = tag;
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

    public String getTag() {
        return tag;
    }
}