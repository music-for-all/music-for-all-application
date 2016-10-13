package com.musicforall.services.followers;

import com.musicforall.model.user.User;
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

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

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
        final User user1 = new User(PASSWORD, "mike@example.com");
        final User user2 = new User(PASSWORD, "john2@example.com");
        userService.save(user1);
        userService.save(user2);
        followerService.follow(user1.getId(), user1.getId());
        assertEquals(0, followerService.getFollowingId(user1.getId()).size());

        followerService.follow(user1.getId(), user2.getId());
        assertNotNull(followerService.getFollowingId(user1.getId()));
    }

    @Test
    public void testUnfollow() {
        final User user = new User(PASSWORD, "test@example.com");
        final User user_followers = new User(PASSWORD, "mail1@example.com");
        userService.save(user);
        userService.save(user_followers);
        followerService.follow(user_followers.getId(), user.getId());
        followerService.unfollow(user_followers.getId(), user.getId());

        assertEquals(0, followerService.getFollowingId(user_followers.getId()).size());
    }

    @Test
    public void testGetFollowerId() {
        final User user = new User(PASSWORD, "anna@example.com");
        final User user_followers = new User(PASSWORD, "james@example.com");
        userService.save(user);
        userService.save(user_followers);

        followerService.follow(user_followers.getId(), user.getId());
        assertEquals(1, followerService.getFollowersId(user.getId()).size());

        followerService.unfollow(user_followers.getId(), user.getId());
        assertEquals(0, followerService.getFollowersId(user.getId()).size());
    }

    @Test
    public void testGetFollowingId() {
        final User user = new User(PASSWORD, "tasha@example.com");
        final User user_followers = new User(PASSWORD, "abrams@example.com");
        userService.save(user);
        userService.save(user_followers);
        followerService.follow(user_followers.getId(), user.getId());
        assertEquals(1, followerService.getFollowingId(user_followers.getId()).size());

        followerService.unfollow(user_followers.getId(), user.getId());
        assertEquals(0, followerService.getFollowingId(user_followers.getId()).size());
    }

}
