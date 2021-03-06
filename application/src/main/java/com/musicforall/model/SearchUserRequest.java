package com.musicforall.model;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by Pavel Podgorniy on 11.10.2016.
 */
public class SearchUserRequest {

    @Size(min = 2, max = 16)
    @Pattern(regexp = "^(^[a-zA-Z\\p{InCyrillic}][a-zA-Z0-9-_\\.\\p{InCyrillic}]+)$")
    private String username;

    private String firstName;

    private String lastName;


    public SearchUserRequest() {
    }

    public SearchUserRequest(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public SearchUserRequest(String username) {
        this.username = username;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    @Override
    public String toString() {
        return "User{" +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
