package com.musicforall.services.user;

import com.musicforall.model.User;
import com.musicforall.util.ServicesTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.Collection;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

/**
 * Created by Pukho on 22.06.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        UserTestExecutionListener.class})
public class UserServiceTest {

    public static final String USER_1 = "user1";

    public static final String USER_NOT_EXIST = "user_not_exist";

    public static final String USER = "user";

    @Autowired
    private UserService userService;

    @Test
    public void testSaveUser() {
        final User user = new User("Masha", "123456789", "masha@example.com");
        userService.save(user);

        assertNotNull(user.getId());
        assertNotNull(userService.isUserExist(user.getId()));
    }

    @Test
    public void testGetIdUserByUsername() {
        final Integer userId = userService.getIdByUsername(USER_1);
        assertEquals(userService.get(userId).getUsername(), USER_1);
        assertNull(userService.getIdByUsername(USER_NOT_EXIST));
    }

    @Test
    public void testGetUserByUserame() {
        assertEquals(userService.getByUsername(USER_1).getUsername(), USER_1);
        assertNull(userService.getByUsername(USER_NOT_EXIST));
    }

    @Test
    public void testIsUserExist() {
        assertTrue(userService.isUserExist(USER_1));
        final Integer userId = userService.getIdByUsername(USER_1);
        assertTrue(userService.isUserExist(userId));

        final User user = new User("user3", "12345789", "user3@example.com");
        assertFalse(userService.isUserExist(user.getUsername()));
        assertFalse(userService.isUserExist(user.getId()));
    }

    @Test
    public void testUserDelete() {
        final User user = userService.getByUsername("user2");
        userService.delete(user.getId());

        assertNull(userService.get(user.getId()));
    }

    @Test
    public void testGetUser() {
        final Integer userId = userService.getIdByUsername(USER);
        final User user = userService.get(userId);

        assertEquals(user.getUsername(), USER);
        assertNotNull(userService.get(userId));
        assertNull(userService.getByUsername(USER_NOT_EXIST));
    }

    @Test
    public void testFindAll() {
        Collection<User> users = userService.findAll();
        assertSame(users.size(), 4);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUsername() {
        final UserDetails user = ((UserDetailsService) userService).loadUserByUsername(USER_1);
        assertEquals(user.getUsername(), USER_1);
        assertNotNull(((UserDetailsService) userService).loadUserByUsername(USER_NOT_EXIST));
    }
}
