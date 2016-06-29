package com.musicforall.services;

import com.musicforall.model.User;
import com.musicforall.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Pukho on 28.06.2016.
 */
@Component
public class ServicesTestBoostrap {
        @Autowired
        UserService userService;

        private boolean bootstraped;

        Lock lock = new ReentrantLock();

        public void fillDatabase() {
            if (bootstraped) return;
            lock.lock();

            userService.save(new User("user", "password"));
            userService.save(new User("user1", "password1", "user1@gmail.com"));
            userService.save(new User("user2", "password2"));


            bootstraped = true;
            lock.unlock();
        }
}

