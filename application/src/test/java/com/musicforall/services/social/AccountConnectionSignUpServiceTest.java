package com.musicforall.services.social;

import com.musicforall.services.message.Mails;
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

import javax.validation.ConstraintViolationException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        doNothing().when(publisher).publishEvent(any());
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
        UserProfile userProfile = new UserProfile("", " ,", "Fg", " ", "dd@mail.com", "");
        when(connection.fetchUserProfile()).thenReturn(userProfile);

        String username = signUpService.execute(connection);
        assertEquals(username, userProfile.getEmail());
    }

    @Test(expected = ConstraintViolationException.class)
    public void testSaveWithSmallUsername() {
        UserProfile userProfile = new UserProfile("", " ,", "1name", " ", "1name@mail.com", "o");
        when(connection.fetchUserProfile()).thenReturn(userProfile);

        signUpService.execute(connection);
    }


    @Test(expected = ConstraintViolationException.class)
    public void testSaveWithBigUsername() {
        UserProfile userProfile = new UserProfile("", " ,", "2name", " ", "2name@mail.com", "12345678901234567");
        when(connection.fetchUserProfile()).thenReturn(userProfile);

        signUpService.execute(connection);
    }

    @Test
    public void testSaveWithExistingEmail() {
        UserProfile userProfile = new UserProfile("", " ,", "Fgname", " ", EXISTING_USER_EMAIL, "");
        when(connection.fetchUserProfile()).thenReturn(userProfile);

        String username = signUpService.execute(connection);
        assertEquals(username, userProfile.getEmail());
    }
}
