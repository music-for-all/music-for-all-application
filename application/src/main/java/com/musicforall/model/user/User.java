package com.musicforall.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.musicforall.common.Constants;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.social.security.SocialUserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

/**
 * Created by ilianik on 11.06.2016.
 */
@NamedQueries(
        {
                @NamedQuery(
                        name = User.CHANGE_USER_PASSWORD,
                        query = "UPDATE User user" +
                                " SET user.password = COALESCE(:password, user.password)" +
                                " where user.id = :id"
                ),
                @NamedQuery(
                        name = User.SWITCH_STATE_OF_PUBLIC_RADIO,
                        query = "UPDATE User user" +
                                " SET user.userData.publicRadio = CASE" +
                                " WHEN user.userData.publicRadio = true THEN false ELSE true END" +
                                " where user.id = :userId"
                ),
                @NamedQuery(
                        name = User.UPDATE_USER_DATA,
                        query = "UPDATE User user" +
                                " SET user.userData.username = COALESCE(:username, user.userData.username)," +
                                " user.userData.firstName = COALESCE(:firstName, user.userData.firstName)," +
                                " user.userData.lastName = COALESCE(:lastName, user.userData.lastName)," +
                                " user.userData.picture = COALESCE(:picture, user.userData.picture)," +
                                " user.userData.publicRadio = COALESCE(:publicRadio, user.userData.publicRadio)," +
                                " user.userData.bio = COALESCE(:bio, user.userData.bio)" +
                                " where user.id = :userId"
                )
        }
)
@Entity
@Table(name = "users")
public class User implements SocialUserDetails, Serializable {

    public static final String CHANGE_USER_PASSWORD = "change_user_password";
    public static final String SWITCH_STATE_OF_PUBLIC_RADIO = "switch_state_of_public_radio";
    public static final String UPDATE_USER_DATA = "update_user_data";

    private static final long serialVersionUID = 1959293141381203004L;

    @Id
    @Column(name = Constants.ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 4, max = 128)
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Email
    @Column(unique = true)
    private String email;

    @Embedded
    private UserData userData;

    public User() {
    }

    public User(String password, String email) {
        this.password = password;
        this.email = email;
    }

    public User(String password, String email, UserData userData) {
        this.password = password;
        this.email = email;
        this.userData = userData;
    }

    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_USER");
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    @JsonIgnore
    public String getUserId() {
        return email;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, password, email);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        return Objects.equals(this.id, other.id)
                && Objects.equals(this.password, other.password)
                && Objects.equals(this.email, other.email);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", " + userData +
                '}';
    }
}
