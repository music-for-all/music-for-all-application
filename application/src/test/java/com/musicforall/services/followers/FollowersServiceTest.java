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
public class FollowersServiceTest {

    private static final String PASSWORD = "password";

    @Autowired
    private UserService userService;

    @Autowired
    private FollowerService followerService;

    @Test
    public void testFollow() {
        final User user1 = new User("Mike", PASSWORD, "mike@example.com");
        final User user2 = new User("John2", PASSWORD, "john2@example.com");
        userService.save(user1);
        userService.save(user2);
        followerService.follow(user1.getId(), user2.getId());

        assertNotNull(followerService.getFollowing(user1.getId()));
    }

    @Test
    public void testUnfollow() {
        final User user = new User("John1", PASSWORD, "test@example.com");
        final User user_followers = new User("Spocks", PASSWORD, "mail1@example.com");
        userService.save(user);
        userService.save(user_followers);
        followerService.follow(user_followers.getId(), user.getId());
        followerService.unfollow(user_followers.getId(), user.getId());

        assertEquals(0, followerService.getFollowing(user_followers.getId()).size());
    }

    @Test
    public void testGetFollowerId() {
        final User user = new User("Anna", PASSWORD, "anna@example.com");
        final User user_followers = new User("James", PASSWORD, "james@example.com");
        userService.save(user);
        userService.save(user_followers);

        followerService.follow(user_followers.getId(), user.getId());
        assertEquals(1, followerService.getFollowersId(user.getId()).size());

        followerService.unfollow(user_followers.getId(), user.getId());
        assertEquals(0, followerService.getFollowersId(user.getId()).size());
    }

    @Test
    public void testGetFollower() {
        final User user1 = new User("Johnny", PASSWORD, "tests@example.com");
        final User user2 = new User("Abc", PASSWORD, "abc@example.com");
        final User user3 = new User("Spock", PASSWORD, "mail2@example.com");
        userService.save(user1);
        userService.save(user2);
        userService.save(user3);

        followerService.follow(user3.getId(), user1.getId());
        followerService.follow(user2.getId(), user1.getId());
        assertEquals(2, followerService.getFollowers(user1.getId()).size());

        followerService.unfollow(user3.getId(), user1.getId());
        assertEquals(1, followerService.getFollowers(user1.getId()).size());

        followerService.unfollow(user2.getId(), user1.getId());
        assertEquals(0, followerService.getFollowers(user1.getId()).size());
    }

    @Test
    public void testGetFollowingId() {
        final User user = new User("Tasha", PASSWORD, "tasha@example.com");
        final User user_followers = new User("Abrams", PASSWORD, "abrams@example.com");
        userService.save(user);
        userService.save(user_followers);
        followerService.follow(user_followers.getId(), user.getId());
        assertEquals(1, followerService.getFollowingId(user_followers.getId()).size());

        followerService.unfollow(user_followers.getId(), user.getId());
        assertEquals(0, followerService.getFollowingId(user_followers.getId()).size());
    }

    @Test
    public void testGetFollowing() {
        final User user1 = new User("Tash", PASSWORD, "tash@example.com");
        final User user2 = new User("Jam", PASSWORD, "jam@example.com");
        final User user_followers = new User("Abr", PASSWORD, "abr@example.com");
        userService.save(user1);
        userService.save(user2);
        userService.save(user_followers);

        followerService.follow(user_followers.getId(), user1.getId());
        followerService.follow(user_followers.getId(), user2.getId());
        assertEquals(2, followerService.getFollowing(user_followers.getId()).size());

        followerService.unfollow(user_followers.getId(), user1.getId());
        assertEquals(1, followerService.getFollowing(user_followers.getId()).size());

        followerService.unfollow(user_followers.getId(), user2.getId());
        assertEquals(0, followerService.getFollowing(user_followers.getId()).size());
    }
}
