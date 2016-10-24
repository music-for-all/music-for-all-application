package com.musicforall.services.notification;

import com.musicforall.common.cache.CacheProvider;
import com.musicforall.notifications.DeferredNotification;
import com.musicforall.notifications.Notification;
import com.musicforall.notifications.Notifier;
import com.musicforall.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.atomic.AtomicInteger;

import static com.musicforall.config.CacheConfig.NOTIFICATION;

/**
 * @author IliaNik on 21.10.2016.
 */
@Service
public class NotificationServiceImpl implements NotificationService {
    private static final long TIMEOUT = 60000;

    @Autowired
    @Qualifier(NOTIFICATION)
    private CacheProvider<Integer, AtomicInteger> cache;
    @Autowired
    private Notifier notifier;

    @Override
    public void fire(Integer userId, Notification.Type type) {
        incrementUnreadNum(userId);
        notifier.fire(userId, type);
    }

    @Override
    public void resetUnreadNum() {
        final Integer userId = SecurityUtil.currentUserId();
        cache.put(userId, new AtomicInteger(0));
    }

    @Override
    public DeferredResult subscribe(Notification.Type type, Object timeoutResult) {
        final Integer userId = SecurityUtil.currentUserId();
        final DeferredResult<Integer> result = new DeferredResult<>(TIMEOUT, timeoutResult);
        result.onCompletion(() -> notifier.unsubscribe(userId, type));

        final DeferredNotification<Integer> userNotification = new DeferredNotification<>(result,
                () -> getUnreadNum(userId), type);

        notifier.subscribe(userId, userNotification);
        return result;
    }

    @Override
    public Integer getUnreadNum(Integer userId) {
        final AtomicInteger unreadAtomicNum = cache.get(userId);
        if (unreadAtomicNum == null) {
            return null;
        }
        return unreadAtomicNum.get();
    }

    private void incrementUnreadNum(Integer userId) {
        AtomicInteger numOfUnread = cache.get(userId);
        if (numOfUnread != null) {
            numOfUnread.incrementAndGet();
        } else {
            numOfUnread = new AtomicInteger(1);
        }
        cache.put(userId, numOfUnread);
    }
}
