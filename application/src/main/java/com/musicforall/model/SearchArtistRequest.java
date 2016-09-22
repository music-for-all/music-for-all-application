package com.musicforall.model;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by Pavel Podgorniy on 9/8/2016.
 */
public class SearchArtistRequest {

    @Size(max = 16)
    private String artistName;

    private List<String> tags;

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public SearchArtistRequest() {
    }

    public SearchArtistRequest(String artistName, List<String> tags) {
        this.artistName = artistName;
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "SearchArtistRequest{" +
                "name='" + artistName + '\'' +
                ", tags=" + tags +
                '}';
    }
}
