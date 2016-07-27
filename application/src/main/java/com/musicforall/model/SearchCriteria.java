package com.musicforall.model;

import javax.validation.constraints.Size;
import java.util.List;

public class SearchCriteria {

    @Size(max = 16)
    private String title;

    @Size(max = 16)
    private String artist;

    @Size(max = 16)
    private String album;

    private List<String> tags;

    public SearchCriteria() {
    }

    public SearchCriteria(String title, String artist, String album, List<String> tags) {
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

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    @Override
    public String toString() {
        return "SearchCriteria{" +
                "title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", tags=" + tags +
                '}';
    }
}