package com.musicforall.services.songlist;

import com.musicforall.common.dao.Dao;
import com.musicforall.model.Songlist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by kgavrylchenko on 24.06.16.
 */
@Component
public class SongListTestBoostrap {
    @Autowired
    Dao dao;
    private boolean bootstraped;

    Lock lock = new ReentrantLock();

    public void fillDatabase() {
        if (bootstraped) return;
        lock.lock();

        dao.save(new Songlist(1));
        dao.save(new Songlist(2));
        dao.save(new Songlist(3));

        bootstraped = true;
        lock.unlock();
    }
}
