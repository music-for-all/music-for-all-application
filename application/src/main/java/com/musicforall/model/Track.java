package com.musicforall.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.musicforall.common.Constants;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by ilianik on 11.06.2016.
 */
@NamedQueries({
        @NamedQuery(
                name = Track.ALL_BY_ID_QUERY,
                query = "select t from Track t where t.id in (:ids)"
        )})

@Entity
@Table(name = "tracks")
public class Track implements Serializable {

    public static final String ALL_BY_ID_QUERY = "all_by_id";
    private static final long serialVersionUID = -6851477594231058789L;
    @Id
    @Column(name = Constants.ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToMany
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinTable(name = "track_tag",
            joinColumns = {@JoinColumn(name = "track_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_name")})
    private Set<Tag> tags;

    @Deprecated
    @Size(min = 2, max = 30)
    @Column(name = Constants.NAME, nullable = false)
    private String name;

    @Size(min = 2, max = 30)
    private String title;

    @ManyToOne
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinTable(name = "artists_tracks",
            inverseJoinColumns = {@JoinColumn(name = "artist_name")},
            joinColumns = {@JoinColumn(name = "track_id")})
    private Artist artist;

    @Size(min = 2, max = 30)
    private String album;

    @Size(min = 2, max = 128)
    @Column(name = "location", nullable = false)
    private String location;

    @Column
    private Long size;

    @JsonIgnore
    @ManyToMany
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinTable(name = "playlists_tracks",
            inverseJoinColumns = {@JoinColumn(name = "playlist_id")},
            joinColumns = {@JoinColumn(name = "track_id")})
    private Set<Playlist> playlists;

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

    public Track(String name, String title, Artist artist, String album, String location, Set<Tag> tags) {
        this.name = name;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.location = location;
        this.tags = tags;
    }

    public Set<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(Set<Playlist> playlists) {
        this.playlists = playlists;
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

    private void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Deprecated
    public String getName() {
        return name;
    }

    @Deprecated
    public void setName(String name) {
        this.name = name;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
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
                && Objects.equals(this.artist, other.artist)
                && Objects.equals(this.location, other.location);
    }

    @Override
    public String toString() {
        return "Track{" +
                "id=" + id +
                ", tags=" + tags +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", location='" + location + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}