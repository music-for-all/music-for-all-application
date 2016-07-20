package com.musicforall.model;

/**
 * Created by ilianik on 11.06.2016.
 */

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
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
    @Column(name = "name", nullable = false)
    private String name;

    @Size(min = 2, max = 30)
    private String title;

    @Size(min = 2, max = 30)
    private String artist;

    @Size(min = 2, max = 30)
    private String album;

    @Column(name = "location", nullable = false)
    private String location;

    public Track() {
    }

    public Track(String name, String location, Set<Tag> tags) {
        this.tags = tags;
        this.name = name;
        this.location = location;
    }

    public Track(String name, String location) {
        this.name = name;
        this.location = location;
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
        return Objects.hash(id, tags, name, location);
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
                && Objects.equals(this.name, other.name)
                && Objects.equals(this.location, other.location);
    }

    @Override
    public String toString() {
        return "Track{" +
                "id='" + id + '\'' +
                "tags='" + tags + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}