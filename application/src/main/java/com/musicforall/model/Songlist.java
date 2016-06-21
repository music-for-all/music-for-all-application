package com.musicforall.model;

/**
 * Created by ilianik on 11.06.2016.
 */


import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "songlists")
public class Songlist implements Serializable {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Size(max = 20)
    @Column(name = "name", nullable = false)
    private String name;


    @ManyToMany
    private Set<Song> songs;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    public Songlist() {
    }

    public Songlist(Integer id) {
        this.id = id;
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


    public Set<Song> getSongs() {
        return songs;
    }

    public void setSongs(Set<Song> songs) {
        this.songs = songs;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Songlist songlist = (Songlist) o;

        if (id != null ? !id.equals(songlist.id) : songlist.id != null) return false;
        if (name != null ? !name.equals(songlist.name) : songlist.name != null) return false;
        return !(user != null ? !user.equals(songlist.user) : songlist.user != null);

    }


    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "Songlist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", songs=" + songs +
                ", user=" + user +
                '}';
    }
}
