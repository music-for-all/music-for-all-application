package com.musicforall.services.message;

import com.musicforall.common.dao.Dao;
import com.musicforall.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author IliaNik on 14.08.2016.
 */
@Service
@Transactional
public class MessageBootstrap {
    @Autowired
    private Dao dao;

    private boolean bootstraped;

    private final Lock lock = new ReentrantLock();

    public void fillDatabase() {
        if (bootstraped) {
            return;
        }
        lock.lock();

        dao.save(new User("user", "password1", "nikolsky12@gmail.com"));

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

    public List<User> bootstrapedEntities() {
        return dao.all(User.class);
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }
}