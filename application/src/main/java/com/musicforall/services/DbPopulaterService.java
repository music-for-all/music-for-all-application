package com.musicforall.services;

import com.musicforall.files.manager.FileManager;
import com.musicforall.model.User;
import com.musicforall.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class DbPopulaterService {

    @Autowired
    private UserService userService;

    @Autowired
    private FileManager fileManager;

    @PostConstruct
    private void addSampleUsers() {
        userService.save(new User("dev", "password", "dev@musicforall.com"));
        try {
            fileManager.save(new URL("http://cdndl.zaycev.net/46015/2158629/garbage_-_cherry_lips_(zaycev.net).mp3"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
