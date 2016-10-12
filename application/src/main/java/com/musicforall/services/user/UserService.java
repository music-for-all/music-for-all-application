package com.musicforall.services.user;

import com.musicforall.dto.profile.ProfileData;
import com.musicforall.model.user.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.security.SocialUserDetailsService;

import java.util.Collection;
import java.util.List;

/**
 * Created by Pukho on 16.06.2016.
 */
public interface UserService extends UserDetailsService, SocialUserDetailsService {

    User save(User user);

    Collection<User> saveAll(Collection<User> users);

    void updateUser(User user, ProfileData profileData);

    void updateUserData(User user, ProfileData profileData);

    User get(Integer userId);

    Integer getIdByEmail(String email);

    void delete(Integer userId);

    User getByEmail(String email);

    List<User> findAll();

    List<User> getUsersById(Collection<Integer> usersId);

    List<User> getAllWithSettingsByIds(Collection<Integer> ids);

    User getWithSettingsById(Integer id);

    List<User> getUsersByUsername(String username);
}
