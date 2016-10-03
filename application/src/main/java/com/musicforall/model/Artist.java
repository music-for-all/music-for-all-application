package com.musicforall.model;

/**
 * @author IliaNik on 15.06.2016.
 */

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@NamedQueries(
        {
                @NamedQuery(
                        name = Artist.ARTIST_LIKE_NAME_QUERY,
                        query = "from Artist where artist_name like :artist_name"
                )
        }
)
@Table(name = "artists")
public class Artist implements Serializable {

    public static final String ARTIST_LIKE_NAME_QUERY = "artist_like_name";
    private static final long serialVersionUID = 5787383287840000175L;

    @Id
    @Size(min = 2, max = 30)
    @Column(name = "name", unique = true)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "artist_tag",
            joinColumns = {@JoinColumn(name = "artist_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_name")})
    private Set<Tag> tags;


    public Artist() {
    }

    public Artist(String name) {
        this.name = name;
    }

    public Artist(String name, Set<Tag> tags) {
        this.name = name;
        this.tags = tags;
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
        return Objects.hash(name, tags);
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
        return Objects.equals(this.name, other.name)
                && Objects.equals(this.tags, other.tags);
    }

    @Override
    public String toString() {
        return "Artist{" +
                "name='" + name + '\'' +
                "tags='" + tags + '\'' +
                '}';
    }

}
