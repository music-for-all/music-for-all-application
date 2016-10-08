package com.musicforall.services.user;

import com.musicforall.model.user.User;
import com.musicforall.model.user.UserSettings;
import com.musicforall.dto.profile.ProfileData;
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

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
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

    public static final String USER_EMAIL_1 = "user@example.com";
    public static final String USER = "user";
    public static final String USER_NOT_EXIST = "user_not_exist";
    public static final String USER_EMAIL = "user1@gmail.com";
    public static final String PASSWORD = "password";

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
    public void testUpdateUser() {
        final User user = new User("Mike", "1234567890", "mike@example.com");
        userService.save(user);

        final ProfileData profileData = ProfileData.create().bio("Bio").get();
        userService.update(user, profileData);
        assertEquals(profileData.getBio(), userService.getByEmail(user.getEmail()).getBio());
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
        assertEquals(USER, user.getUsername());
        assertNotNull(userService.loadUserByUsername(USER_NOT_EXIST));
    }

    @Test
    public void testGetUsersById() {
        final User user1 = new User("Johnny", PASSWORD, "testGetUsersById1@example.com");
        final User user2 = new User("Abc", PASSWORD, "testGetUsersById2@example.com");
        final User user3 = new User("Spock", PASSWORD, "testGetUsersById3@example.com");
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
        final User user1 = new User("C_3PO", PASSWORD, "c_3po@example.com");
        userService.save(user1);

        assertNotNull(userService.loadUserByUserId(user1.getEmail()));
    }

    @Test
    public void testGetUsersWithSettings() {
        final UserSettings defaultSettings = new UserSettings(true, "link");
        final User user1 = new User(USER, PASSWORD, "mail1@example.com");
        final User user2 = new User(USER, PASSWORD, "mail2@example.com");
        final User user3 = new User(USER, PASSWORD, "mail3@example.com");

        final List<User> users = asList(user1, user2, user3);
        users.forEach(user -> user.setSettings(defaultSettings));
        userService.saveAll(users);

        final List<Integer> userIds = users.stream().map(User::getId).collect(toList());
        final List<User> usersWithSettings = userService.getAllWithSettingsByIds(userIds);

        final boolean match = usersWithSettings.stream()
                .filter(u -> defaultSettings.equals(u.getSettings()))
                .allMatch(user -> userIds.contains(user.getId()));
        assertTrue(match);
    }

    @Test
    public void testGetUserWithSettings() {
        final UserSettings settings = new UserSettings(true, "link");
        User user = new User(USER, PASSWORD, "testGetUserWithSettings@test.com");
        user.setSettings(settings);

        userService.save(user);

        assertEquals(settings, userService.getWithSettingsById(user.getId()).getSettings());
    }
}
