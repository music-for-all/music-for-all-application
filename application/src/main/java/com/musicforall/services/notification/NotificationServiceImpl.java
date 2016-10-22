package com.musicforall.services.notification;

import com.musicforall.common.cache.CacheProvider;
import com.musicforall.common.notification.Notifier;
import com.musicforall.dto.UserNotification;
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

    @Autowired
    @Qualifier(NOTIFICATION)
    private CacheProvider<Integer, AtomicInteger> cache;

    @Autowired
    private Notifier<UserNotification> notifier;

    @Override
    public void incrementNotifierNum(Integer userId) {
        AtomicInteger numOfUnread = cache.get(userId);
        if (numOfUnread != null) {
            numOfUnread.incrementAndGet();
        } else {
            numOfUnread = new AtomicInteger(1);
        }
        cache.put(userId, numOfUnread);
        notifier.doNotifyWhen(n -> n.getUserId().equals(userId));
    }

    @Override
    public void resetNotifierNum() {
        final Integer userId = SecurityUtil.currentUserId();
        cache.put(userId, new AtomicInteger(0));
    }

    @Override
    public DeferredResult<Integer> getDeferredNotifierNum(Object timeoutResult) {
        final Integer userId = SecurityUtil.currentUserId();
        final DeferredResult result = new DeferredResult(null, timeoutResult);

        final UserNotification userNotification = new UserNotification(result, () -> {
            final AtomicInteger unreadAtomicNum = cache.get(userId);
            if (unreadAtomicNum == null) {
                return null;
            }
            return unreadAtomicNum.get();
        }, userId);

        return notifier.add(userNotification);
    }
}
