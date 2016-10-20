package com.musicforall.web.feed;

import com.musicforall.common.cache.CacheProvider;
import com.musicforall.services.Notifier;
import com.musicforall.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author IliaNik on 31.08.2016.
 */
@Controller
public class FeedController {

    private static final String NUM_OF_UNREAD_NEWS = "num_unread_news ";
    private static final Logger LOG = LoggerFactory.getLogger(FeedController.class);

    @Autowired
    @Qualifier("notification")
    private CacheProvider<String, AtomicInteger> cache;

    @Autowired
    private Notifier notifier;

    @RequestMapping("/feed")
    public String friendsActivity(Model model) {
        final Integer userId = SecurityUtil.currentUserId();
        cache.put(NUM_OF_UNREAD_NEWS + userId, new AtomicInteger(0));
        return "followingHistory";
    }

    @ResponseBody
    @RequestMapping(value = "/num_of_unread", method = RequestMethod.GET)
    public DeferredResult<Integer> getNumOfUnreadNews() {
        //TODO move to service
        final Integer userId = SecurityUtil.currentUserId();
        return notifier.deffer(() -> {
            final AtomicInteger numOfUnread = cache.get(NUM_OF_UNREAD_NEWS + userId);
            return numOfUnread != null ? numOfUnread.get() : null;
        });
    }
}

