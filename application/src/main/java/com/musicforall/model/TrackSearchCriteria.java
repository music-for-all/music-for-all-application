package com.musicforall.model;

import javax.validation.constraints.Size;
import java.util.List;

public class TrackSearchCriteria {

    @Size(max = 16)
    private String title;

    @Size(max = 16)
    private String artist;

    @Size(max = 16)
    private String album;

    private List<String> tags;

    public TrackSearchCriteria() {
    }

    public TrackSearchCriteria(String title, String artist, String album, List<String> tags) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
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
        return "TrackSearchCriteria{" +
                "title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", tags=" + tags +
                '}';
    }
}