package com.musicforall.services.user;

import com.musicforall.common.dao.Dao;
import com.musicforall.model.User;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Created by Pukho on 16.06.2016.
 */
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private Dao dao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public void save(User user) {
        /* Encode the password before saving the user. */
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        dao.save(user);
    }

    @Override
    public User get(Integer id) {
        return dao.get(User.class, id);
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
    public User getByUsername(String username) {
        final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class)
                .add(Property.forName("username").eq(username));

        return dao.getBy(detachedCriteria);
    }

    @Override
    public List<User> findAll() {
        return dao.all(User.class);
    }

    @Override
    public void follow(Integer userId, Integer following_userId) {
        if (userId.equals(following_userId)) {
            LOG.info(String.format("Users can not follow themselves", userId, following_userId));
            return;
        }
        final User user = dao.get(User.class, userId);
        final User user2 = dao.get(User.class, following_userId);
        user.follow(user2);
        dao.save(user);
    }

    @Override
    public void unfollow(Integer userId, Integer following_userId) {
        final User user = dao.get(User.class, userId);
        final User user2 = dao.get(User.class, following_userId);
        user.unfollow(user2);
        dao.save(user);
    }

    @Override
    public Set<User> getFollowers(Integer userId) {
        return dao.get(User.class, userId).getFollowers();
    }

    @Override
    public Set<User> getFollowing(Integer userId) {
        return dao.get(User.class, userId).getFollowing();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final User user = getByUsername(username);
        if (user == null) {
            LOG.info(String.format("User %s not found", username));
            throw new UsernameNotFoundException("Username not found");
        }
        return user;
    }
}
