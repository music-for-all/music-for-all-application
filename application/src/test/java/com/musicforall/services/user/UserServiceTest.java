package com.musicforall.services.user;

import com.musicforall.model.User;
import com.musicforall.services.user.UserService;
import com.musicforall.util.JpaServicesTestConfig;
import com.musicforall.util.ServicesTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Pukho on 22.06.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testUserSave(){
        User user = new User("Jhon", "1234567890", "Jhon@gmail.com");
        userService.save(user);
        assertNotNull(userService.get(user.getId()));
    }

    @Test
    public void testUserDelete(){
        User user = new User("Jhon2", "1234567890", "Jhon2@gmail.com");
        userService.save(user); userService.delete(user.getId());
        assertNull(userService.get(user.getId()));
    }

    @Test
    public void testIsUserExist(){
        User user = new User("Jhon4", "1234567890", "Jhon4@gmail.com");
        User user2 = new User("Jhon5", "1234567890", "Jhon5@gmail.com");
        userService.save(user);

        assertTrue(userService.isUserExist(user.getId()));
        assertFalse(userService.isUserExist(user2.getId()));
    }
}
