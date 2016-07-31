package com.musicforall.services.user;

import com.musicforall.model.User;
import com.musicforall.util.ServicesTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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

    @Autowired
    private UserBootstrap userBootstrap;

    @Test
    public void testSaveUser() {
        final User user = new User("Masha", "123456789", "masha@example.com");
        userService.save(user);

        final Integer id = user.getId();
        assertTrue(id > 0);
        assertNotNull(userService.get(id));
    }

    @Test
    public void testGetIdUserByUsername() {
        final Integer userId = userService.getIdByUsername(USER_1);
        assertEquals(userService.get(userId).getUsername(), USER_1);
        assertNull(userService.getIdByUsername(USER_NOT_EXIST));
    }

    @Test
    public void testGetUserByUsername() {
        assertEquals(userService.getByUsername(USER_1).getUsername(), USER_1);
        assertNull(userService.getByUsername(USER_NOT_EXIST));
    }

    @Test
    public void testUserDelete() {
        final User user = new User("Test", "123456789", "test@example.com");
        userService.save(user);
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
        final List<User> usersInDB = userBootstrap.bootstrapedEntities();
        final List<User> users = userService.findAll();
        assertEquals(usersInDB.size(), users.size());
        assertTrue(users.stream().allMatch(usersInDB::contains));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUsername() {
        final UserDetails user = userService.loadUserByUsername(USER_1);
        assertEquals(user.getUsername(), USER_1);
        assertNotNull(userService.loadUserByUsername(USER_NOT_EXIST));
    }

    @Test
    public void testFollowUsers() {
        final User user = new User("John", "password", "test@example.com");
        final User user_followers = new User("Mike", "password", "mail@example.com");
        userService.save(user);
        userService.save(user_followers);
        userService.follow(user_followers.getId(), user.getId());
        assertEquals(1, userService.get(user.getId()).getFollowers().size());

        assertEquals(1, userService.get(user_followers.getId()).getFollowing().size());

        userService.unfollow(user_followers.getId(), user.getId());
        assertEquals(0, userService.getFollowers(user.getId()).size());

        assertEquals(0, userService.getFollowing(user_followers.getId()).size());
    }
}
