package com.musicforall.services.social;

import com.musicforall.model.User;
import com.musicforall.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;
import org.springframework.stereotype.Component;

/**
 * Created by Andrey on 8/11/16.
 */

@Component
public class AccountConnectionSignUpService implements ConnectionSignUp {

    @Autowired
    private UserService userService;

    private final int MAX_LENGTH_NAME = 16;

    public String execute(Connection<?> connection) {
        final UserProfile profile = connection.fetchUserProfile();
        final User user = new User();
        if ((profile.getUsername() == null) ||
                (profile.getUsername().length() < 2) ||
                (profile.getUsername().length() > MAX_LENGTH_NAME)) {
            user.setUsername(profile.getFirstName());
        } else {
            user.setUsername(profile.getUsername());
        }
        user.setEmail(profile.getEmail());
        user.setPassword(KeyGenerators.string().generateKey());
        userService.save(user);
        return user.getEmail();
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
