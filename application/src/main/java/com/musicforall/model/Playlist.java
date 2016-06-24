package com.musicforall.model;

/**
 * Created by ilianik on 11.06.2016.
 */


import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "playlists")
public class Playlist implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 20)
    @Column(name = "name", nullable = false)
    private String name;


    @ManyToMany
    private Set<Track> tracks;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    public Playlist() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Set<Track> getTracks() {
        return tracks;
    }

    public void setTracks(Set<Track> tracks) {
        this.tracks = tracks;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, name, tracks, user);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Playlist other = (Playlist) obj;
        return Objects.equals(this.id, other.id)
                && Objects.equals(this.name, other.name)
                && Objects.equals(this.tracks, other.tracks)
                && Objects.equals(this.user, other.user);
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tracks=" + tracks +
                ", user=" + user +
                '}';
    }
}
