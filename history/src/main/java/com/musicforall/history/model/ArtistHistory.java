package com.musicforall.history.model;

/**
 * Created by Pukho on 19.10.2016.
 */

import com.musicforall.common.Constants;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

@Entity
@Table(name = "artist_history")
public class ArtistHistory {

    @Id
    @Column(name = Constants.ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "artist_name")
    private String artistName;

    @Column(name = "count")
    private Integer playsCount;

    public ArtistHistory() {

    }

    public ArtistHistory(String artist_name, Integer playsCount) {
        this.artistName = artist_name;
        this.playsCount = playsCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public Integer getPlaysCount() {
        return playsCount;
    }

    public void setPlaysCount(Integer playsCount) {
        this.playsCount = playsCount;
    }
}
