package com.musicforall.services.user;

import com.musicforall.common.Constants;
import com.musicforall.common.dao.Dao;
import com.musicforall.model.SearchUserRequest;
import com.musicforall.model.user.User;
import com.musicforall.model.user.UserData;
import com.musicforall.services.SearchCriteriaFactory;
import org.hibernate.FetchMode;
import com.musicforall.dto.profile.ProfileData;
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
import java.util.stream.Collectors;

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
    public User save(User user) {
        /* Encode the password before saving the user. */
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        dao.save(user);
        if (user.getUserData() != null && user.getUserData().getUserId() == null) {
            final Map<String, Object> params = new HashMap<>();
            params.put("userId", user.getId());
            params.put("id", user.getUserData().getId());
            dao.update(UserData.UPDATE_USER_ID, params);
            return user;
        }
        return user;
    }

    @Override
    public Collection<User> saveAll(Collection<User> users) {
        return dao.saveAll(users);
    }

    @Override
    public void updateUserData(Integer userId, ProfileData profileData) {
        final Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("username", profileData.getUsername());
        params.put("firstName", profileData.getFirstName());
        params.put("lastName", profileData.getLastName());
        params.put("picture", profileData.getPicture());
        params.put("bio", profileData.getBio());
        params.put("publicRadio", profileData.getPublicRadio());

        dao.update(UserData.UPDATE_USER_DATA, params);
    }

    @Override
    public void updateUser(Integer userId, ProfileData profileData) {
        final Map<String, Object> params = new HashMap<>();
        params.put("id", userId);
        if (profileData.getPassword() != null) {
            profileData.setPassword(passwordEncoder.encode(profileData.getPassword()));
        }
        params.put("password", profileData.getPassword());

        dao.update(User.UPDATE_USER, params);
    }

    @Override
    public UserData getUserData(Integer userId) {
        final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserData.class)
                .add(Property.forName("userId").eq(userId));

        return dao.getBy(detachedCriteria);
    }

    @Override
    public List<UserData> getUsersDataById(Collection<Integer> usersId) {
        final Map<String, Object> params = new HashMap<>();
        params.put("usersId", usersId);
        return dao.getAllByNamedQuery(UserData.class, UserData.USERS_DATA_BY_USER_IDS, params);
    }

    @Override
    public List<UserData> getAllUserDataLike(SearchUserRequest searchCriteria) {
        final DetachedCriteria detachedCriteria =
                SearchCriteriaFactory.createUserSearchCriteria(searchCriteria);
        return dao.getAllBy(detachedCriteria);
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
                .setFetchMode("userData", FetchMode.JOIN)
                .add(Property.forName(Constants.EMAIL).eq(email));

        return dao.getBy(detachedCriteria);
    }

    @Override
    public List<User> findAll() {
        return dao.all(User.class);
    }

    @Override
    public List<User> getAllLike(SearchUserRequest searchCriteria) {
        final DetachedCriteria detachedCriteria =
                SearchCriteriaFactory.createUserSearchCriteria(searchCriteria);
        final List<UserData> userData = dao.getAllBy(detachedCriteria);
        return getUsersById(userData.stream().map(UserData::getUserId).collect(Collectors.toList()));
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
    public List<User> getAllWithUserDataByIds(Collection<Integer> ids) {
        final Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        return dao.getAllByNamedQuery(User.class, User.USERS_BY_IDS_WITH_DATA_QUERY, params);
    }

    @Override
    public User getWithUserDataById(Integer id) {
        final Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return dao.getByNamedQuery(User.class, User.USER_BY_ID_WITH_DATA_QUERY, params);
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
