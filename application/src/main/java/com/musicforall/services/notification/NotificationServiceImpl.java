package com.musicforall.services.notification;

import com.musicforall.common.cache.CacheProvider;
import com.musicforall.common.notification.Notifier;
import com.musicforall.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author IliaNik on 21.10.2016.
 */
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    @Qualifier("notification")
    private CacheProvider<Integer, AtomicInteger> cache;

    @Autowired
    private Notifier notifier;

    public void incrementNotifierNum(Integer userId) {
        AtomicInteger numOfUnread = cache.get(userId);
        if (numOfUnread != null) {
            numOfUnread.incrementAndGet();
        } else {
            numOfUnread = new AtomicInteger(1);
        }
        cache.put(userId, numOfUnread);
    }

    public void resetNotifierNum() {
        final Integer userId = SecurityUtil.currentUserId();
        cache.put(userId, new AtomicInteger(0));
    };

    public DeferredResult<Integer> getDeferredNotifierNum() {
        final Integer userId = SecurityUtil.currentUserId();
        return notifier.deffer(() -> {
            final AtomicInteger numOfUnread = cache.get(userId);
            return numOfUnread != null ? numOfUnread.get() : null;
        });
    };
}
