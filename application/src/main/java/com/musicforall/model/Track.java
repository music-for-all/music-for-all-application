package com.musicforall.model;

/**
 * Created by ilianik on 11.06.2016.
 */

import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tracks")
public class Track implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToMany
    @Cascade(CascadeType.SAVE_UPDATE)
    private Set<Tag> tags;

    @Size(min = 2, max = 30)
    @Column(name = "artist", nullable = false)
    private String artist;

    @Size(min = 2, max = 30)
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "location", nullable = false)
    private String location;

    public Track() {
    }

    public Track(Set<Tag> tags, String artist, String name, String location) {
        this.tags = tags;
        this.artist= artist;
        this.name = name;
        this.location = location;
    }

    public Track(String artist, String name, String location) {
        this.artist= artist;
        this.name = name;
        this.location = location;
    }

    public void addTags(Set<Tag> tags) {
        if (this.tags == null) {
            this.tags = new HashSet<>();
        }
        this.tags.addAll(tags);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tags, artist, name, location);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Track other = (Track) obj;
        return Objects.equals(this.id, other.id)
                && Objects.equals(this.tags, other.tags)
                && Objects.equals(this.artist, other.artist)
                && Objects.equals(this.name, other.name)
                && Objects.equals(this.location, other.location);
    }

    @Override
    public String toString() {
        return "Track{" +
                "id='" + id + '\'' +
                "tags='" + tags + '\'' +
                ", artist='" + artist + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }


}