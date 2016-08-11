package com.musicforall.services.social;

import com.musicforall.model.User;
import com.musicforall.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public String execute(Connection<?> connection) {
        UserProfile profile = connection.fetchUserProfile();
        User user = new User();
        if ((profile.getUsername() == null) || (profile.getUsername().isEmpty())) {
            user.setUsername(profile.getFirstName());
        } else {
            user.setUsername(profile.getUsername());
        }
        user.setEmail(profile.getEmail());
        user.setPassword(KeyGenerators.string().generateKey());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(profile.getUsername(), null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        userService.save(user);
        return user.getUsername();
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
