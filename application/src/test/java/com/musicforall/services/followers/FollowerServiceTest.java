package com.musicforall.services.followers;

import com.musicforall.model.User;
import com.musicforall.services.follower.FollowerService;
import com.musicforall.services.user.UserService;
import com.musicforall.util.ServicesTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;

/**
 * Created by andrey on 8/2/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@ActiveProfiles("dev")
public class FollowerServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private FollowerService followerService;

    @Test
    public void testFollow() {
        final User user1 = new User("Mike", "123456789", "mike@example.com");
        final User user2 = new User("John2", "password2", "john2@example.com");
        userService.save(user1);
        userService.save(user2);
        followerService.follow(user1.getId(), user2.getId());

        assertNotNull(followerService.getFollowingId(user1.getId()));
    }

    @Test
    public void testUnfollow() {
        final User user = new User("John1", "password3", "test@example.com");
        final User user_followers = new User("Spocks", "passwor", "mail1@example.com");
        userService.save(user);
        userService.save(user_followers);
        followerService.follow(user_followers.getId(), user.getId());
        followerService.unfollow(user_followers.getId(), user.getId());
        assertEquals(0, followerService.getFollowerId(user.getId()).size());
    }

    @Test
    public void testGetIdFollower() {
        final User user = new User("Johnny", "password", "tests@example.com");
        final User user_followers = new User("Spock", "password", "mail2@example.com");
        userService.save(user);
        userService.save(user_followers);

        followerService.follow(user_followers.getId(), user.getId());
        assertEquals(1, followerService.getFollowerId(user.getId()).size());

        followerService.unfollow(user_followers.getId(), user.getId());
        assertEquals(0, followerService.getFollowerId(user.getId()).size());
    }

    @Test
    public void testGetIdFollowing() {
        final User user = new User("John_Wick", "password", "johnW@example.com");
        final User user_followers = new User("Spock_Father", "password", "spockF@example.com");
        userService.save(user);
        userService.save(user_followers);

        followerService.follow(user_followers.getId(), user.getId());
        assertEquals(1, followerService.getFollowingId(user_followers.getId()).size());

        followerService.unfollow(user_followers.getId(), user.getId());
        assertEquals(0, followerService.getFollowingId(user_followers.getId()).size());
    }

    @Test
    public void testGetFollower() {
        final User user = new User("Anna", "password", "anna@example.com");
        final User user_followers = new User("James", "password", "james@example.com");
        userService.save(user);
        userService.save(user_followers);

        followerService.follow(user_followers.getId(), user.getId());
        assertEquals(1, followerService.getFollower(user.getId()).size());

        followerService.unfollow(user_followers.getId(), user.getId());
        assertEquals(0, followerService.getFollower(user.getId()).size());
    }

    @Test
    public void testGetFollowing() {
        final User user = new User("Tasha", "password", "tasha@example.com");
        final User user_followers = new User("Abrams", "password", "abrams@example.com");
        userService.save(user);
        userService.save(user_followers);

        followerService.follow(user_followers.getId(), user.getId());
        assertEquals(1, followerService.getFollowing(user_followers.getId()).size());

        followerService.unfollow(user_followers.getId(), user.getId());
        assertEquals(0, followerService.getFollowing(user_followers.getId()).size());
    }
}
