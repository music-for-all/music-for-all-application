package com.musicforall.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.musicforall.common.Constants;
import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.social.security.SocialUserDetails;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

import static org.hibernate.annotations.CascadeType.DELETE;
import static org.hibernate.annotations.CascadeType.SAVE_UPDATE;

/**
 * Created by ilianik on 11.06.2016.
 */
@NamedQueries(
        {
                @NamedQuery(
                        name = User.USERS_BY_IDS_WITH_SETTINGS_QUERY,
                        query = "from User u left join fetch u.settings where u.id in (:ids)"
                ),
                @NamedQuery(
                        name = User.USER_BY_ID_WITH_SETTINGS_QUERY,
                        query = "from User u left join fetch u.settings where u.id = :id"
                )
        }
)
@Entity
@Table(name = "users")
public class User implements SocialUserDetails, Serializable {

    public static final String USERS_BY_IDS_WITH_SETTINGS_QUERY = "users_by_ids_with_settings";
    public static final String USER_BY_ID_WITH_SETTINGS_QUERY = "user_by_id_with_settings";
    private static final long serialVersionUID = 1959293141381203004L;

    @Id
    @Column(name = Constants.ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 2, max = 16)
    @Pattern(regexp = "^(^[a-zA-Z\\p{InCyrillic}][a-zA-Z0-9-_\\.\\p{InCyrillic}]+)$")
    @Column(nullable = false)
    private String username;

    @Size(min = 4, max = 128)
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Email
    @Column(unique = true)
    private String email;

    private String firstName;

    private String lastName;

    @OneToOne(fetch = FetchType.LAZY)
    @Cascade({SAVE_UPDATE, DELETE})
    private UserSettings settings;

    @OneToOne(fetch = FetchType.LAZY)
    @Cascade({SAVE_UPDATE, DELETE})
    private UserAchievements achievements;

    public User() {
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(String username, String password, String email, UserSettings settings) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.settings = settings;
    }

    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public UserSettings getSettings() {
        return settings;
    }

    public void setSettings(UserSettings settings) {
        this.settings = settings;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, email);
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
                && Objects.equals(this.username, other.username)
                && Objects.equals(this.password, other.password)
                && Objects.equals(this.email, other.email);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
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

    public UserAchievements getAchievements() {
        return achievements;
    }

    public void setAchievements(UserAchievements achievements) {
        this.achievements = achievements;
    }
}
