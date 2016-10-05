package com.musicforall.model;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by Andrey on 9/26/16.
 */
public class ProfileData {

    @Pattern(regexp = "^(^[a-zA-Z\\p{InCyrillic}][a-zA-Z0-9-_\\.\\p{InCyrillic}]+)$")
    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String picture;

    @Size(max = 140)
    private String bio;

    public ProfileData() {
    }

    public ProfileData(String username, String password, String firstName,
                       String lastName, String picture, String bio) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.picture = picture;
        this.bio = bio;
    }

    public String getPassword() {
        return password;
    }

    public ProfileData setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public ProfileData setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public ProfileData setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getBio() {
        return bio;
    }

    public ProfileData setBio(String bio) {
        this.bio = bio;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public ProfileData setUsername(String username) {
        this.username = username;
        return this;
    }

    public ProfileData setPicture(String picture) {
        this.picture = picture;
        return this;
    }

    public String getPicture() {
        return picture;
    }

    public boolean encodePassword() {
        if (password != null && !password.isEmpty()) {
            return true;
        }
        return false;
    }

    public User update(User currentUser) {
        if (username != null) {
            currentUser.setUsername(username);
        }
        if (password != null && !password.isEmpty()) {
            currentUser.setPassword(password);
        }
        if (firstName != null) {
            currentUser.setFirstName(firstName);
        }
        if (lastName != null) {
            currentUser.setLastName(lastName);
        }
        if (bio != null) {
            currentUser.setBio(bio);
        }
        if (picture != null) {
            currentUser.setPicture(picture);
        }
        return currentUser;
    }

    @Override
    public String toString() {
        return "ProfileData{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", bio='" + bio + '\'' +
                '}';
    }
}
