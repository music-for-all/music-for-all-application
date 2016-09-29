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
@Table(name = "user_config")
public class UserConfig implements Serializable {

    @Id
    @Column(name = Constants.ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "public_radio")
    private boolean isPublicRadio;
    @Column
    private String picture;

    public UserConfig() {
    }

    public UserConfig(boolean isPublicRadio, String picture) {
        this.isPublicRadio = isPublicRadio;
        this.picture = picture;
    }

    public boolean isPublicRadio() {
        return isPublicRadio;
    }

    public void setPublicRadio(boolean isPublicRadio) {
        this.isPublicRadio = isPublicRadio;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isPublicRadio);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final UserConfig other = (UserConfig) obj;
        return Objects.equals(this.isPublicRadio, other.isPublicRadio);
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("isPublicRadio", isPublicRadio)
                .add("picture", picture)
                .toString();
    }
}
