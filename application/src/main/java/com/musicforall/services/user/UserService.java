package com.musicforall.services.user;

import com.musicforall.model.User;

/**
 * Created by Pukho on 16.06.2016.
 */
public interface UserService {
    void save(User user);

    User get(Integer id);

    boolean isUserExist(Integer userId);

    Integer getIdByName(String name);

    void delete(Integer userId);

    boolean isUserExist(String name);

    User getByName(String name);

}
