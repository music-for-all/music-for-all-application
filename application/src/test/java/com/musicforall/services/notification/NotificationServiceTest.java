package com.musicforall.services.notification;

import com.musicforall.model.user.User;
import com.musicforall.notifications.Notification;
import com.musicforall.services.user.UserService;
import com.musicforall.util.ServicesTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.Collection;
import java.util.stream.IntStream;

import static com.musicforall.common.Constants.PASSWORD;
import static com.musicforall.notifications.Notification.Type.FOLLOWER;
import static com.musicforall.notifications.Notification.Type.NEW_STREAM;
import static org.junit.Assert.*;

/**
 * @author ENikolskiy.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class})
@ActiveProfiles("dev")
public class NotificationServiceTest {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;

    @Test
    public void subscribeTest() {
        final User user = userService.save(new User(PASSWORD, "subscribeTest@test.com"));
        notificationService.subscribe(user.getId(), null);

        final int count = 10;
        IntStream.range(0, count).forEach(i -> {
            notificationService.fire(user.getId(), FOLLOWER);
            notificationService.fire(user.getId(), NEW_STREAM);
        });
        final Collection<Notification> unread = notificationService.getUnread(user.getId());

        assertNotNull(unread);
        assertTrue(unread.stream().allMatch(n -> n.getType() == FOLLOWER
                || n.getType() == NEW_STREAM));
        assertEquals(count, unread.stream()
                .filter(n -> n.getType() == FOLLOWER)
                .findFirst()
                .get().getCount().get());

        notificationService.clearNotifications(user.getId());
        assertNull(notificationService.getUnread(user.getId()));
    }
}