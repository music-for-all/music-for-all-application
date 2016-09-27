package com.musicforall.model;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by Andrey on 9/26/16.
 */
public class ProfileData {

    public ProfileData() {
    }

    public ProfileData(String username, String password, String picture, String firstName,
                       String lastName, String bio) {
        this.username = username;
        this.password = password;
        this.picture = picture;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bio = bio;
    }

    @Pattern(regexp = "^(^[a-zA-Z\\p{InCyrillic}][a-zA-Z0-9-_\\.\\p{InCyrillic}]+)$")
    private String username;

    private String password;

    private String picture;

    private String firstName;

    private String lastName;

    @Size(max = 140)
    private String bio;

    public User update(User currentUser){
        if (username != null){
            currentUser.setUsername(username);
        }
        if (password != null){
            currentUser.setPassword(password);
        }
        if (picture != null){
            currentUser.setPicture(picture);
        }
        if (firstName != null){
            currentUser.setFirstName(firstName);
        }
        if (lastName != null) {
            currentUser.setLastName(lastName);
        }
        if (bio != null){
            currentUser.setBio(bio);
        }
        return currentUser;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "ProfileData{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", picture='" + picture + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", bio='" + bio + '\'' +
                '}';
    }
}
