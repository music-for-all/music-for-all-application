package com.musicforall.services;

import com.musicforall.model.Songlist;
import com.musicforall.model.User;

import java.util.Set;

/**
 * Created by Pukho on 16.06.2016.
 */
public interface UserService {
    void save(User user);
    User get(Integer id);
    boolean isUserExist(Integer userId);
    Integer getIdByName(String name);
    void delete(Integer userId);
}
