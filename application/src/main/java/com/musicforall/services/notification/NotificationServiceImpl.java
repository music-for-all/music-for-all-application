package com.musicforall.services.notification;

import com.musicforall.common.cache.CacheProvider;
import com.musicforall.common.notification.Notifier;
import com.musicforall.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            return unreadAtomicNum.get();
        }, new ResponseEntity<>(HttpStatus.REQUEST_TIMEOUT));
    }
}
