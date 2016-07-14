package com.musicforall.services.user;

import com.musicforall.common.dao.Dao;
import com.musicforall.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Pukho on 28.06.2016.
 */
@Service
@Transactional
public class UserBootstrap {
    @Autowired
    private Dao dao;

    private boolean bootstraped;

    private final Lock lock = new ReentrantLock();

    public void fillDatabase() {
        if (bootstraped) {
            return;
        }
        lock.lock();

        dao.save(new User("user", "password", "user@example.com"));
        dao.save(new User("user1", "password1", "user1@gmail.com"));
        dao.save(new User("user2", "password2", "user2@example.com"));


        bootstraped = true;
        lock.unlock();
    }

    public void clean() {
        lock.lock();
        dao.all(User.class)
                .stream()
                .forEach(dao::delete);
        bootstraped = false;
        lock.unlock();
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }
}

