package com.musicforall.services.social;

import com.musicforall.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Created by Andrey on 8/11/16.
 */
@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("dev")
public class SimpleSocialUserDetailsServiceTest {

    private static final String USER_EMAIL = "john.smith@gmail.com";
    private static final String USER_NAME = "John";
    private static final String PASSWORD = "password";

    private SimpleSocialUserDetailService service;

    @Mock
    private UserDetailsService userDetailsServiceMock;

    @Before
    public void setUp() {
        service = new SimpleSocialUserDetailService(userDetailsServiceMock);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadByUserIdUserDetailsNotFound() {
        when(userDetailsServiceMock.loadUserByUsername(USER_NAME)).thenThrow(new UsernameNotFoundException(""));

        userDetailsServiceMock.loadUserByUsername(USER_NAME);

        verifyNoMoreInteractions(userDetailsServiceMock);
    }

    @Test
    public void loadByUserIdUserDetailsFound() {
        User user = new User();
        user.setUsername(USER_NAME);
        user.setEmail(USER_EMAIL);
        user.setPassword(PASSWORD);

        when(userDetailsServiceMock.loadUserByUsername(USER_NAME)).thenReturn(user);

        service.loadUserByUserId(USER_NAME);

        verify(userDetailsServiceMock, times(1)).loadUserByUsername(USER_NAME);
        verifyNoMoreInteractions(userDetailsServiceMock);
    }
}
