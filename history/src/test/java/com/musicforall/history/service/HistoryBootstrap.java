package com.musicforall.history.service;

import com.musicforall.common.dao.Dao;
import com.musicforall.history.model.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author IliaNik on 28.07.2016.
 */
@Service
@Transactional
public class HistoryBootstrap {

    @Autowired
    private Dao dao;

    private boolean bootstraped;

    private final Lock lock = new ReentrantLock();

    public void fillDatabase() {
        if (bootstraped) {
            return;
        }

        lock.lock();
        bootstraped = true;
        lock.unlock();
    }

    public void clean() {
        lock.lock();
        final List<History> all = dao.all(History.class);
        all.stream().forEach(dao::delete);
        bootstraped = false;
        lock.unlock();
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }
}
