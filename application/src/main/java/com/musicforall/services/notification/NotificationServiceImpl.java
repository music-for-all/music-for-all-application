package com.musicforall.services.notification;

import com.musicforall.common.cache.CacheProvider;
import com.musicforall.common.notification.Notifier;
import com.musicforall.dto.UserNotification;
import com.musicforall.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;
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
    private Notifier<UserNotification> notifier;

    @Override
    public void incrementUnreadNum(Integer userId) {
        AtomicInteger numOfUnread = cache.get(userId);
        if (numOfUnread != null) {
            numOfUnread.incrementAndGet();
        } else {
            numOfUnread = new AtomicInteger(1);
        }
        cache.put(userId, numOfUnread);
        notifier.doNotifyWhere(n -> userId.equals(n.getUserId()));
    }

    @Override
    public void resetUnreadNum() {
        final Integer userId = SecurityUtil.currentUserId();
        cache.put(userId, new AtomicInteger(0));
    }

    @Override
    public DeferredResult getDeferredUnreadNum(Object timeoutResult) {
        final Integer userId = SecurityUtil.currentUserId();
        final DeferredResult<Integer> result = new DeferredResult<>(TIMEOUT, timeoutResult);

        Callable<Integer> callback = () -> getUnreadNum(userId);

        final UserNotification<Integer> userNotification = new UserNotification<>(result, callback, userId);

        notifier.add(userNotification);
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
}
