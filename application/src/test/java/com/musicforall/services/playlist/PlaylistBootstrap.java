package com.musicforall.services.playlist;

import com.musicforall.common.dao.Dao;
import com.musicforall.model.Playlist;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by kgavrylchenko on 24.06.16.
 */
@Service
@Transactional
public class PlaylistBootstrap {

    @Autowired
    private Dao dao;

    @Autowired
    public void setDao(@Autowired @Qualifier("main_session") SessionFactory sessionFactory) {
        dao.setSessionFactory(sessionFactory);
    }

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
        final List<Playlist> all = dao.all(Playlist.class);
        all.stream().forEach(dao::delete);
        bootstraped = false;
        lock.unlock();
    }
}
