package com.musicforall.services.user;

import com.musicforall.common.dao.Dao;
import com.musicforall.model.User;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Pukho on 16.06.2016.
 */
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

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
        try {
            dao.get(User.class, userId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Integer getIdByName(String name) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class)
                .add(Property.forName("name").eq(name));

        User user = dao.getBy(detachedCriteria);
        return user.getId();
    }

    @Override
    public void delete(Integer userId) {
        User user = dao.get(User.class, userId);
        dao.delete(user);
    }
}


