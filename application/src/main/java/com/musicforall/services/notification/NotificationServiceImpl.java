package com.musicforall.services.notification;

import com.musicforall.common.cache.CacheProvider;
import com.musicforall.common.notification.Notifier;
import com.musicforall.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author IliaNik on 21.10.2016.
 */
@Service("notification")
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    @Qualifier("notifications")
    private CacheProvider<Integer, AtomicInteger> cache;

    @Autowired
    private Notifier notifier;

    @Override
    public void incrementNotifierNum(Integer userId) {
        AtomicInteger numOfUnread = cache.get(userId);
        if (numOfUnread != null) {
            numOfUnread.incrementAndGet();
        } else {
            numOfUnread = new AtomicInteger(1);
        }
        cache.put(userId, numOfUnread);
    }

    @Override
    public void resetNotifierNum() {
        final Integer userId = SecurityUtil.currentUserId();
        cache.put(userId, new AtomicInteger(0));
    }


    @Override
    public DeferredResult<Integer> getDeferredNotifierNum() {
        final Integer userId = SecurityUtil.currentUserId();
        return notifier.deffer(() -> {
            final AtomicInteger unreadAtomicNum = cache.get(userId);
            if (unreadAtomicNum == null) {
                return null;
            }
            final Integer unreadNum = unreadAtomicNum.get();
            if (unreadNum.equals(notifier.checkingNum)) {
                return null;
            }
            notifier.checkingNum = unreadNum;
            return unreadNum;
        });
    }
}
