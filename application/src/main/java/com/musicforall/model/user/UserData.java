package com.musicforall.model.user;

import com.google.common.base.MoreObjects;
import com.musicforall.common.Constants;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author ENikolskiy.
 */
@NamedQueries(
        {
                @NamedQuery(
                        name = UserData.UPDATE_USER_DATA,
                        query = "UPDATE UserData data" +
                                " SET data.username = COALESCE(:username, data.username)," +
                                " data.firstName = COALESCE(:firstName, data.firstName)," +
                                " data.lastName = COALESCE(:lastName, data.lastName)," +
                                " data.picture = COALESCE(:picture, data.picture)," +
                                " data.publicRadio = COALESCE(:publicRadio, data.publicRadio)," +
                                " data.bio = COALESCE(:bio, data.bio)" +
                                " where data.userId.id =:userId"
                ),
                @NamedQuery(
                        name = UserData.USERS_DATA_BY_USER_IDS,
                        query = "from UserData data where data.userId.id in (:usersId)"
                ),
                @NamedQuery(
                        name = UserData.SWITCH_STATE_OF_PUBLIC_RADIO,
                        query = "UPDATE UserData data" +
                                " SET data.publicRadio = CASE WHEN data.publicRadio = true THEN false ELSE true END" +
                                " where data.userId.id = :userId"
                )
        }
)
@Entity
@Table(name = "user_data")
public class UserData implements Serializable {

    public static final String USERS_DATA_BY_USER_IDS = "users_data_by_user_ids";
    public static final String UPDATE_USER_DATA = "update_user_data";
    public static final String SWITCH_STATE_OF_PUBLIC_RADIO = "switch_state_of_public_radio";

    @Id
    @Column(name = Constants.ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Integer id;

    @Size(min = 2, max = 16)
    @Pattern(regexp = "^(^[a-zA-Z\\p{InCyrillic}][a-zA-Z0-9-_\\.\\p{InCyrillic}]+)$")
    @Column(nullable = false)
    private String username;

    @Column
    private String picture;

    private String firstName;

    private String lastName;

    @Size(max = 140)
    private String bio = "";

    @Column(name = "public_radio")
    private boolean publicRadio;

    @OneToOne
    private User userId;

    public UserData() {
    }

    public UserData(User user, String username) {
        this.userId = user;
        this.username = username;
    }

    public UserData(User user, String username, String firstName, String lastName,
                    String picture, String bio, boolean isPublicRadio) {
        this.userId = user;
        this.username = username;
        this.bio = bio;
        this.publicRadio = isPublicRadio;
        this.firstName = firstName;
        this.lastName = lastName;
        this.picture = picture;
    }

    private void setId(Integer id) {
        this.id = id;
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

    public Integer getUserId() {
        return userId.getId();
    }

    public void setUserId(User userId) {
        this.userId = userId;
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
                .add("id", id)
                .add("username", username)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("bio", bio)
                .add("picture", picture)
                .add("publicRadio", publicRadio)
                .add("userId", getUserId())
                .toString();
    }
}
