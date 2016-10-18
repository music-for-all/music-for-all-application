package com.musicforall.model.user;

import com.google.common.base.MoreObjects;
import com.musicforall.common.Constants;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Andrey
 */
@Embeddable
public class UserData implements Serializable {

    @Size(min = 2, max = 16)
    @Pattern(regexp = "^(^[a-zA-Z\\p{InCyrillic}][a-zA-Z0-9-_\\.\\p{InCyrillic}]+)$")
    private String username;

    @Column
    private String picture;

    private String firstName;

    private String lastName;

    @Size(max = 140)
    private String bio = "";

    @Column(name = "public_radio")
    private boolean publicRadio;

    public UserData() {
    }

    public UserData(String username) {
        this.username = username;
    }

    public UserData(String username, String firstName, String lastName,
                    String picture, String bio, boolean isPublicRadio) {
        this.username = username;
        this.bio = bio;
        this.publicRadio = isPublicRadio;
        this.firstName = firstName;
        this.lastName = lastName;
        this.picture = picture;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getUsername() {
        if (username == null) {
            return Constants.USER;
        }
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, firstName, lastName, bio, picture, publicRadio);
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
        return Objects.equals(this.username, other.username)
                && Objects.equals(this.firstName, other.firstName)
                && Objects.equals(this.lastName, other.lastName)
                && Objects.equals(this.bio, other.bio)
                && Objects.equals(this.picture, other.picture)
                && Objects.equals(this.publicRadio, other.publicRadio);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("username", username)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("bio", bio)
                .add("picture", picture)
                .add("publicRadio", publicRadio)
                .toString();
    }
}
