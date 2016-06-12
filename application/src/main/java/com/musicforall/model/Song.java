package com.musicforall.model;

/**
 * Created by ilianik on 11.06.2016.
 */

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "songs")
public class Song implements Serializable {

    @Column(name = "id")
    private String id;

    @Column(name = "tags")
    private String tags;

    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    public Song() {
    }

    public Song(String tags, String name, String location) {
        this.tags = tags;
        this.name = name;
        this.location = location;
    }

    public Song(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Song song = (Song) o;

        if (name != null ? !name.equals(song.name) : song.name != null) return false;
        if (tags != null ? !tags.equals(song.tags) : song.tags != null) return false;
        if (location != null ? !location.equals(song.location) : song.location != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "Song{" +
                "id='" + id + '\'' +
                "tags='" + tags + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}