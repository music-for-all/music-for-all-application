package com.musicforall.model;

/**
 * @author IliaNik on 15.06.2016.
 */

import com.musicforall.common.Constants;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;

@Entity
@NamedQueries(
        {
                @NamedQuery(
                        name = Artist.ARTIST_LIKE_NAME_QUERY,
                        query = "from Artist a where a.name like :artistName"
                )
        }
)
@Table(name = "artists")
public class Artist implements Serializable {

    public static final String ARTIST_LIKE_NAME_QUERY = "artist_like_name";
    private static final long serialVersionUID = 5787383287840000175L;

    @Id
    @Column(name = Constants.ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 2, max = 30)
    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinTable(name = "artist_tag",
            joinColumns = {@JoinColumn(name = "artist_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_name")})
    private Set<Tag> tags = new HashSet<>();

    public Artist() {
    }

    public Artist(String name) {
        this.name = name;
    }

    public Artist(String name, Set<Tag> tags) {
        this.name = name;
        this.tags = tags;
    }

    public static Artist createDummyArtist() {
        final String artistName = Constants.NAME + UUID.randomUUID().toString();
        return new Artist(artistName, Collections.emptySet());
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

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public void extendTags(Set<Tag> tags) {
        this.tags.addAll(tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, tags);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Artist other = (Artist) obj;
        return Objects.equals(this.id, other.id)
                && Objects.equals(this.name, other.name)
                && Objects.equals(this.tags, other.tags);
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tags=" + tags +
                '}';
    }
}
