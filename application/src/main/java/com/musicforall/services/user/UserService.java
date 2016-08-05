package com.musicforall.services.user;

import com.musicforall.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * Created by Pukho on 16.06.2016.
 */
public interface UserService extends UserDetailsService {
    void save(User user);

    User get(Integer id);

    Integer getIdByUsername(String username);

    void delete(Integer userId);

    User getByUsername(String username);

    List<User> findAll();

    List<User> getUsersById(List<Integer> usersId);
}
