package com.musicforall.model;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.musicforall.common.Constants;
import com.musicforall.model.user.User;
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
                name = Playlist.ALL_BY_ID_QUERY,
                query = "select p from Playlist p where p.id in (:ids)"
        )})

@Entity
@Table(name = "playlists")
public class Playlist implements Serializable {

    public static final String ALL_BY_ID_QUERY = "all_playlists_by_id";
    private static final long serialVersionUID = 3556491830874885637L;

    @Id
    @Column(name = Constants.ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 20)
    @Column(name = Constants.NAME, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "playlists")
    @Cascade(CascadeType.SAVE_UPDATE)
    private Set<Track> tracks;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Playlist() {
    }

    public Playlist(String name, Set<Track> tracks, User user) {
        this.name = name;
        this.tracks = tracks;
        this.user = user;
    }

    public void addTracks(Set<Track> tracks) {
        if (this.tracks == null) {
            this.tracks = new HashSet<>();
        }
        this.tracks.addAll(tracks);
    }

    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
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
