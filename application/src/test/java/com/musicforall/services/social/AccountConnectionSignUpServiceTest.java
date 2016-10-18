package com.musicforall.services.social;

import com.musicforall.services.mail.Mails;
import com.musicforall.services.user.UserService;
import com.musicforall.services.user.UserTestExecutionListener;
import com.musicforall.util.ServicesTestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

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

    public static final String EXISTING_USER_EMAIL = "user@example.com";

    @Autowired
    @InjectMocks
    private AccountConnectionSignUpService signUpService;
    @Mock
    private Connection connection;
    @Mock
    private Mails mails;
    @Mock
    private ApplicationEventPublisher publisher;
    @Mock
    private UserService userService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveWithUsername() {
        UserProfile userProfile = new UserProfile("", " ,", " ", " ", "ddd@mail.com", "Lol");
        when(connection.fetchUserProfile()).thenReturn(userProfile);

        String username = signUpService.execute(connection);
        assertEquals(username, userProfile.getEmail());
    }

    @Test
    public void testSaveWithoutUsername() {
        UserProfile userProfile = new UserProfile("", " ,", "Fg", " ", "dd@mail.com", null);
        when(connection.fetchUserProfile()).thenReturn(userProfile);

        String username = signUpService.execute(connection);
        assertEquals(username, userProfile.getEmail());
    }

    @Test
    public void testSaveWithIncorrectUsername() {
        UserProfile userProfile = new UserProfile("", " ,", "1name", " ", "1name@mail.com", "o");
        when(connection.fetchUserProfile()).thenReturn(userProfile);

        signUpService.execute(connection);
        assertEquals(userProfile.getFirstName(), userProfile.getFirstName());

        UserProfile userProfile2 = new UserProfile("", " ,", "2name", " ", "2name@mail.com", "12345678901234567");
        when(connection.fetchUserProfile()).thenReturn(userProfile2);

        signUpService.execute(connection);
        assertEquals(userProfile2.getFirstName(), userProfile2.getFirstName());
    }

    @Test
    public void testSaveWithExistingEmail() {
        UserProfile userProfile = new UserProfile("", " ,", "Fgname", " ", EXISTING_USER_EMAIL, "");
        when(connection.fetchUserProfile()).thenReturn(userProfile);

        String username = signUpService.execute(connection);
        assertEquals(username, userProfile.getEmail());
    }
}
