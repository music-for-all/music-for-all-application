package com.musicforall.services.social;

import com.musicforall.services.user.UserService;
import com.musicforall.services.user.UserTestExecutionListener;
import com.musicforall.util.ServicesTestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import javax.validation.ConstraintViolationException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by Andrey on 8/11/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        UserTestExecutionListener.class})
@ActiveProfiles("dev")
public class AccountConnectionSignUpServiceTest {

    @Autowired
    private UserService userService;

    private AccountConnectionSignUpService accountConnectionSignUpService;

    @Before
    public void setUp() {
        accountConnectionSignUpService = new AccountConnectionSignUpService();
        accountConnectionSignUpService.setUserService(userService);
    }

    @Test
    public void testSaveWithUsername() {
        Connection<?> connection = Mockito.mock(Connection.class);

        UserProfile userProfile = new UserProfile("", " ,", " ", " ", "ddd@mail.com", "Lol");
        when(connection.fetchUserProfile()).thenReturn(userProfile);

        String username = accountConnectionSignUpService.execute(connection);
        assertEquals(username, userProfile.getEmail());
    }

    @Test
    public void testSaveWithoutUsername() {
        Connection<?> connection = Mockito.mock(Connection.class);

        UserProfile userProfile = new UserProfile("", " ,", "Fg", " ", "dd@mail.com", "");
        when(connection.fetchUserProfile()).thenReturn(userProfile);

        String username = accountConnectionSignUpService.execute(connection);
        assertEquals(username, userProfile.getEmail());
    }

    @Test(expected = ConstraintViolationException.class)
    public void testSaveWithSmallUsername() {
        Connection<?> connection = Mockito.mock(Connection.class);

        UserProfile userProfile = new UserProfile("", " ,", "1name", " ", "1name@mail.com", "o");
        when(connection.fetchUserProfile()).thenReturn(userProfile);

        accountConnectionSignUpService.execute(connection);
    }


    @Test(expected = ConstraintViolationException.class)
    public void testSaveWithBigUsername() {
        Connection<?> connection = Mockito.mock(Connection.class);

        UserProfile userProfile = new UserProfile("", " ,", "2name", " ", "2name@mail.com", "12345678901234567");
        when(connection.fetchUserProfile()).thenReturn(userProfile);

        accountConnectionSignUpService.execute(connection);
    }
}
