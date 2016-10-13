package com.musicforall.services.user;

import com.musicforall.model.SearchUserRequest;
import com.musicforall.model.user.User;
import com.musicforall.model.user.UserData;
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
        final User user = new User("123456789", "masha@example.com", new UserData(USER));
        userService.save(user);

        final Integer id = user.getId();
        assertTrue(id > 0);
        assertNotNull(userService.get(id));
    }

    @Test
    public void testSaveUserDataWitOtherUserId() {
        final User user = new User("1234567891", "mash@example.com", new UserData(USER));
        user.getUserData().setUserId(0);
        userService.save(user);

        final Integer id = user.getId();
        assertTrue(id > 0);
        assertNotEquals(id, userService.get(id).getUserData().getUserId());
    }

    @Test
    public void testUpdateUser() {
        final User user = new User("12345678901", "mikel@example.com", new UserData(USER));
        userService.save(user);

        final ProfileData profileData = ProfileData.create().password("password").get();
        userService.updateUser(user, profileData);
        assertEquals(profileData.getPassword(), userService.getByEmail(user.getEmail()).getPassword());
    }

    @Test
    public void testUpdateUserData() {
        final User user = new User("1234567890", "mike@example.com", new UserData(USER));
        userService.save(user);

        final ProfileData profileData = ProfileData.create().picture("picture").get();
        userService.updateUserData(user.getId(), profileData);
        assertEquals(profileData.getPicture(), userService.getWithUserDataById(user.getId()).getUserData().getPicture());
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
        final User user = new User("123456789", "test@example.com");
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
        assertEquals(USER_EMAIL_1, user.getUsername());
        assertNotNull(userService.loadUserByUsername(USER_NOT_EXIST));
    }

    @Test
    public void testGetUsersById() {
        final User user1 = new User(PASSWORD, "testGetUsersById1@example.com");
        final User user2 = new User(PASSWORD, "testGetUsersById2@example.com");
        final User user3 = new User(PASSWORD, "testGetUsersById3@example.com");
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
        final User user1 = new User(PASSWORD, "c_3po@example.com");
        userService.save(user1);

        assertNotNull(userService.loadUserByUserId(user1.getEmail()));
    }

    @Test
    public void testGetUsersWithData() {
        final User user1 = new User(PASSWORD, "mail1@example.com", new UserData(USER));
        final User user2 = new User(PASSWORD, "mail2@example.com", new UserData(USER));
        final User user3 = new User(PASSWORD, "mail3@example.com", new UserData(USER));

        final List<User> users = asList(user1, user2, user3);
        userService.saveAll(users);

        final List<Integer> userIds = users.stream().map(User::getId).collect(toList());
        final List<User> usersWithSettings = userService.getAllWithUserDataByIds(userIds);

        final boolean match = usersWithSettings.stream()
                .filter(u -> new UserData(USER).equals(u.getUserData()))
                .allMatch(user -> userIds.contains(user.getId()));
        assertTrue(match);
    }

    @Test
    public void testGetUserWithData() {
        User user = new User(PASSWORD, "testGetUserWithSettings@test.com", new UserData(USER));
        userService.save(user);
        assertEquals(user.getUserData().getUsername(),
                userService.getWithUserDataById(user.getId()).getUserData().getUsername());
    }

    @Test
    public void testGetUserLike() {
        User user = new User(PASSWORD, "testGetUserLike@test.com", new UserData("test_name"));
        userService.save(user);
        SearchUserRequest searchUserByUsername = new SearchUserRequest("test");
        assertEquals(userService.getAllLike(searchUserByUsername).size(), 1);
    }
}
