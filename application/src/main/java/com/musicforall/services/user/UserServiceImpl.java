package com.musicforall.services.user;

import com.musicforall.common.Constants;
import com.musicforall.common.dao.Dao;
import com.musicforall.model.user.User;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by Pukho on 16.06.2016.
 */
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private Dao dao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void save(User user) {
        /* Encode the password before saving the user. */
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        dao.save(user);
    }

    @Override
    public Collection<User> saveAll(Collection<User> users) {
        return dao.saveAll(users);
    }

    @Override
    public User get(Integer id) {
        return dao.get(User.class, id);
    }

    @Override
    public Integer getIdByEmail(String email) {
        final User user = getByEmail(email);
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
    public User getByEmail(String email) {
        final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class)
                .add(Property.forName(Constants.EMAIL).eq(email));

        return dao.getBy(detachedCriteria);
    }

    @Override
    public List<User> findAll() {
        return dao.all(User.class);
    }

    @Override
    public List<User> getUsersById(Collection<Integer> usersId) {
        if (usersId.isEmpty()) {
            return new ArrayList<>();
        }
        final Disjunction disjunction = Restrictions.disjunction();
        for (final Integer follower : usersId) {
            disjunction.add(Property.forName(Constants.ID).eq(follower));
        }
        final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class)
                .add(disjunction);
        return dao.getAllBy(detachedCriteria);
    }

    @Override
    public List<User> getUsersWithOptionsByIds(Collection<Integer> ids) {
        final Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        return dao.getAllByNamedQuery(User.class, User.USERS_BY_IDS_WITH_OPTIONS_QUERY, params);
    }

    @Override
    public User getWithOptionsById(Integer id) {
        final Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return dao.getByNamedQuery(User.class, User.USER_BY_ID_WITH_OPTIONS_QUERY, params);
    }

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return loadUserByUserId(email);
    }

    @Override
    public User loadUserByUserId(String email) throws UsernameNotFoundException {
        final User user = getByEmail(email);
        if (user == null) {
            LOG.info("User {} not found", email);
            throw new UsernameNotFoundException("Email not found");
        }
        return user;
    }

    @Override
    public List<User> getUsersByUsername(String username) {
        final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class)
                .add(Property.forName("username").eq(username));

        return dao.getAllBy(detachedCriteria);
    }
}
