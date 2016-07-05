package com.musicforall.services;

import com.musicforall.model.User;
import com.musicforall.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class DbPopulaterService {

    @Autowired
    private UserService userService;

    @PostConstruct
    private void addSampleUsers() {
        userService.save(new User(
                "dev",
                new StandardPasswordEncoder().encode("password"),
                "dev@musicforall.com"));
    }
}
