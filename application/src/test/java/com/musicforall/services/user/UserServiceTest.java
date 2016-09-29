package com.musicforall.services.user;

import com.musicforall.model.user.User;
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

import java.util.ArrayList;
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

    public static final String USER_EMAIL_1 = "user@example.com";

    public static final String USER_1 = "user";

    public static final String USER_NOT_EXIST = "user_not_exist";

    public static final String USER_EMAIL = "user1@gmail.com";

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
    public void testGetIdUserByEmail() {
        final Integer userId = userService.getIdByEmail(USER_EMAIL_1);
        assertEquals(userService.get(userId).getEmail(), USER_EMAIL_1);
        assertNull(userService.getIdByEmail(USER_NOT_EXIST));
    }

    @Test
    public void testGetUserByUsername() {
        assertEquals(userService.getByEmail(USER_EMAIL_1).getEmail(), USER_EMAIL_1);
        assertNull(userService.getByEmail(USER_NOT_EXIST));
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
        final Integer userId = userService.getIdByEmail(USER_EMAIL);
        final User user = userService.get(userId);

        assertEquals(USER_EMAIL, user.getEmail());
        assertNotNull(userService.get(userId));
        assertNull(userService.getByEmail(USER_NOT_EXIST));
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
        final UserDetails user = userService.loadUserByUsername(USER_EMAIL_1);
        assertEquals(USER_1, user.getUsername());
        assertNotNull(userService.loadUserByUsername(USER_NOT_EXIST));
    }

    @Test
    public void testGetUsersById() {
        final User user1 = new User("Johnny", "password", "tests@example.com");
        final User user2 = new User("Abc", "password", "abc@example.com");
        final User user3 = new User("Spock", "123455", "mail2@example.com");
        final List<Integer> users = new ArrayList<>();
        userService.save(user1);
        users.add(user1.getId());
        assertEquals(users.size(), userService.getUsersById(users).size());
        userService.save(user2);
        users.add(user2.getId());
        assertEquals(users.size(), userService.getUsersById(users).size());
        userService.save(user3);
        users.add(user3.getId());
        assertEquals(users.size(), userService.getUsersById(users).size());
        userService.delete(user3.getId());
        assertEquals(2, userService.getUsersById(users).size());
    }

    @Test
    public void testLoadUserByUserId() {
        final User user1 = new User("C_3PO", "password", "c_3po@example.com");
        userService.save(user1);

        assertNotNull(userService.loadUserByUserId(user1.getEmail()));
    }
}
