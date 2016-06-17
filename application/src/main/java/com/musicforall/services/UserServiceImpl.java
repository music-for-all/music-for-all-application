package com.musicforall.services;

import com.musicforall.common.dao.Dao;
import com.musicforall.model.Songlist;
import com.musicforall.model.User;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.soap.SOAPBinding;
import java.util.HashSet;
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

    @Override
    public void save(User user) {
        dao.save(user);
    }

    @Override
    public User get(Integer id) {
        return dao.get(User.class, id);
    }

    @Override
    public Set<Songlist> getAllUserSonglist(User user) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Songlist.class)
                .add(Property.forName("user").eq(user));
        List<Songlist> usersSonglists = dao.getAllBy(detachedCriteria);
        return new HashSet<Songlist>(usersSonglists);
    }

    @Override
    public void update() {
        //here is nothing yet
    }

    @Override
    public Integer getIdByName(String name) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class)
                .add(Property.forName("name").eq(name));

        User user = dao.getBy(detachedCriteria);
        return user.getId();
    }

    @Override
    public void delete(User user) {
        dao.delete(user);
    }
}
