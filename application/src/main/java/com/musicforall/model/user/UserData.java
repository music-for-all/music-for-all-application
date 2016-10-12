package com.musicforall.model.user;

import com.google.common.base.MoreObjects;
import com.musicforall.common.Constants;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author ENikolskiy.
 */
@Entity
@Table(name = "user_settings")
public class UserData implements Serializable {

    @Id
    @Column(name = Constants.ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Integer id;

    @Column(name = "public_radio")
    private boolean publicRadio;

    @Column
    private String picture;

    public UserData() {
    }

    public UserData(boolean isPublicRadio, String picture) {
        this.publicRadio = isPublicRadio;
        this.picture = picture;
    }

    public Integer getId() {
        return id;
    }

    public boolean isPublicRadio() {
        return publicRadio;
    }

    public void setPublicRadio(boolean isPublicRadio) {
        this.publicRadio = isPublicRadio;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public int hashCode() {
        return Objects.hash(publicRadio);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final UserData other = (UserData) obj;
        return Objects.equals(this.publicRadio, other.publicRadio);
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("publicRadio", publicRadio)
                .add("picture", picture)
                .toString();
    }
}
