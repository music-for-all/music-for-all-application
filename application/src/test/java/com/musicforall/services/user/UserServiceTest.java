package com.musicforall.services.user;

import com.musicforall.config.HibernateConfigDev;
import com.musicforall.config.SpringRootConfiguration;
import com.musicforall.model.User;
import com.musicforall.util.ServicesTestConfig;
import org.hibernate.Hibernate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.*;

/**
 * Created by Pukho on 22.06.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        UserTestExecutionListener.class})
@ActiveProfiles("dev")
public class UserServiceTest {

    public static final String USER_1 = "user1";

    public static final String USER_NOT_EXIST = "user_not_exist";

    public static final String USER = "user";

    @Autowired
    private UserService userService;

    @Test
    public void testSaveUser() {
        final User user = new User("Masha", "123456789");
        userService.save(user);

        assertNotNull(user.getId());
        assertNotNull(userService.isUserExist(user.getId()));
    }

    @Test
    public void testGetIdUserByName() {
        final Integer userId = userService.getIdByName(USER_1);
        assertEquals(userService.get(userId).getName(), USER_1);
        assertNull(userService.getIdByName(USER_NOT_EXIST));
    }

    @Test
    public void testGetUserByName() {
        assertEquals(userService.getByName(USER_1).getName(), USER_1);
        assertNull(userService.getByName(USER_NOT_EXIST));
    }

    @Test
    public void testIsUserExist() {
        assertTrue(userService.isUserExist(USER_1));
        final Integer userId = userService.getIdByName(USER_1);
        assertTrue(userService.isUserExist(userId));

        final User user = new User("user3", "12345789");
        assertFalse(userService.isUserExist(user.getName()));
        assertFalse(userService.isUserExist(user.getId()));
    }

    @Test
    public void testUserDelete() {
        final User user = userService.getByName("user2");
        userService.delete(user.getId());

        assertNull(userService.get(user.getId()));
    }

    @Test
    public void testGetUser() {
        final Integer userId = userService.getIdByName(USER);
        final User user = userService.get(userId);

        assertEquals(user.getName(), USER);
        assertNotNull(userService.get(userId));
    }
}
