package com.musicforall.services.social;

import com.musicforall.model.user.User;
import com.musicforall.model.user.UserData;
import com.musicforall.services.mail.Mails;
import com.musicforall.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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

    private final int MAX_NAME_LENGTH = 16;
    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private Mails mails;

    @Override
    public String execute(Connection<?> connection) {
        final UserProfile profile = connection.fetchUserProfile();
        final User user = new User();
        user.setEmail(profile.getEmail());
        if ((profile.getUsername() == null) ||
                (profile.getUsername().length() < 2) ||
                (profile.getUsername().length() > MAX_NAME_LENGTH)) {
            user.setUserData(new UserData(profile.getFirstName(), profile.getFirstName(),
                    profile.getLastName(), connection.getImageUrl(), "", false));
        } else {
            user.setUserData(new UserData(profile.getUsername(), profile.getFirstName(),
                    profile.getLastName(), connection.getImageUrl(), "", false));
        }
        user.setPassword(KeyGenerators.string().generateKey());
        if (userService.getByEmail(user.getEmail()) == null) {
            userService.save(user);
            publisher.publishEvent(mails.welcomeMail(user));
        }
        return user.getEmail();
    }
}
