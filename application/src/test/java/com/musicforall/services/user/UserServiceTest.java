package com.musicforall.services.user;

import com.musicforall.model.User;
import com.musicforall.util.ServicesTestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.*;

/**
 * Created by Pukho on 22.06.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserBootstrap boostraped;

    @Before
    public void installBoostrap() {
        boostraped.fillDatabase();
    }

    @Test
    public void testSaveUser() {
        User user = new User("Masha", "123456789");
        userService.save(user);

        assertNotNull(user.getId());
        assertNotNull(userService.isUserExist(user.getId()));
    }

    @Test
    public void testGetIdUserByName() {
        Integer userId = userService.getIdByName("user1");
        assertEquals(userService.get(userId).getName(), "user1");
        assertNull(userService.getIdByName("user_not_exist"));
    }

    @Test
    public void testGetUserByName() {
        assertEquals(userService.getByName("user1").getName(), "user1");
        assertNull(userService.getByName("user_not_exist"));
    }

    @Test
    public void testIsUserExist() {
        assertTrue(userService.isUserExist("user1"));
        Integer userId = userService.getIdByName("user1");
        assertTrue(userService.isUserExist(userId));

        User user = new User("user3", "12345789");
        assertFalse(userService.isUserExist(user.getName()));
        assertFalse(userService.isUserExist(user.getId()));
    }

    @Test
    public void testUserDelete() {
        User user = userService.getByName("user2");
        userService.delete(user.getId());

        assertNull(userService.get(user.getId()));
    }

}
