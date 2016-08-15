package com.musicforall.services.user;

import com.musicforall.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * Created by Pukho on 16.06.2016.
 */
public interface UserService extends UserDetailsService {

    /**
     * Saves the provided User instance.
     * @param user the user to be saved
     */
    void save(User user);

    /**
     * Retrieves a user with the given id.
     * @param id the id of the user
     * @return the retrieved user
     */
    User get(Integer id);

    /**
     * Retrieves an id of a user with the given username.
     * @param username the username of the user
     * @return the id of retrieved user
     */
    Integer getIdByUsername(String username);

    /**
     * Deletes a user with the specified id from the database.
     * @param userId the id of the user to be deleted
     */
    void delete(Integer userId);

    /**
     * Retrieves a user with the given username.
     * @param username the id of the user
     * @return the retrieved user
     */
    User getByUsername(String username);

    /**
     * Retrieves all User records stored in the database.
     * @return a collection of users
     */
    List<User> findAll();

    /**
     * Retrieves all users the id of which is in the given list of ids.
     * @return a collection of users
     */
    List<User> getUsersById(List<Integer> usersId);
}
