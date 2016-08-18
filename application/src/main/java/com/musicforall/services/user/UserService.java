package com.musicforall.services.user;

import com.musicforall.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.security.SocialUserDetailsService;

import java.util.List;

/**
 * Created by Pukho on 16.06.2016.
 */
public interface UserService extends UserDetailsService, SocialUserDetailsService {

    /**
     * Saves the provided User instance.
     * @param user the user to be saved
     */
    void save(User user);

    /**
     * Retrieves a user with the given id.
     * @param userId the id of the user
     * @return the retrieved user
     */
    User get(Integer userId);

    /**
     * Retrieves an id of a user with the given username.
     * @param email the username of the user
     * @return the id of retrieved user
     */
    Integer getIdByEmail(String email);

    /**
     * Deletes a user with the specified id from the database.
     * @param userId the id of the user to be deleted
     */
    void delete(Integer userId);

    /**
     * Retrieves a user with the given username.
     * @param email the id of the user
     * @return the retrieved user
     */
    User getByEmail(String email);

    /**
     * Retrieves all User records stored in the database.
     * @return a collection of users
     */
    List<User> findAll();

    List<User> getUsersById(List<Integer> usersId);

    List<User> getUsersByUsername(String username);
}
