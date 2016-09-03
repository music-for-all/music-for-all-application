package com.musicforall.model;

/**
 * @author IliaNik on 15.06.2016.
 */

import com.musicforall.common.Constants;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@NamedQueries(
        {
                @NamedQuery(
                        name = Tag.ALL_LIKE_NAME_QUERY,
                        query = "from Tag where lower(name) like :name"
                ),
                @NamedQuery(
                        name = Tag.POPULAR_TAGS_BY_TRACK_ID_QUERY,
                        query = "select tag.name from Track track join track.tags tag where track.id in (:ids)" +
                                " group by tag.name" +
                                " order by count(tag.name) desc"
                )
        }
)
@Table(name = "tags")
public class Tag implements Serializable {

    public static final String POPULAR_TAGS_BY_TRACK_ID_QUERY = "popular_tags_in_tracks";

    public static final String ALL_LIKE_NAME_QUERY = "all_like_name";

    private static final long serialVersionUID = 5787383287840000175L;
    @Id
    @Size(min = 2, max = 30)
    @Column(name = Constants.NAME, unique = true)
    private String name;

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Tag other = (Tag) obj;
        return Objects.equals(this.name, other.name);
    }

    @Override
    public String toString() {
        return "Tag{" +
                "name='" + name + '\'' +
                '}';
    }
}
