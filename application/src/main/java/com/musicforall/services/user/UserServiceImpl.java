package com.musicforall.services.user;

import com.musicforall.common.dao.Dao;
import com.musicforall.model.User;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * Created by Pukho on 16.06.2016.
 */
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private Dao dao;

    @Override
    public void save(User user) {
        dao.save(user);
    }

    @Override
    public User get(Integer id) {
        return dao.get(User.class, id);
    }


    @Override
    public boolean isUserExist(Integer userId) {
        final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class)
                .add(Property.forName("id").eq(userId));

        return dao.getBy(detachedCriteria) != null;
    }

    @Override
    public Integer getIdByUsername(String username) {
        final User user = getByUsername(username);
        if (user != null) {
            return user.getId();
        }
        return null;
    }

    @Override
    public void delete(Integer userId) {
        final User user = dao.get(User.class, userId);
        dao.delete(user);
    }

    @Override
    public boolean isUserExist(String username) {
        return getByUsername(username) != null;
    }

    @Override
    public User getByUsername(String username) {
        final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class)
                .add(Property.forName("username").eq(username));

        return dao.getBy(detachedCriteria);
    }

    @Override
    public Collection<User> findAll() {
        return dao.all(User.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = getByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("Username not found");
        return user;
    }
}
