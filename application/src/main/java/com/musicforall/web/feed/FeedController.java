package com.musicforall.web.feed;

import com.musicforall.common.cache.CacheProvider;
import com.musicforall.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author IliaNik on 31.08.2016.
 */
@Controller
public class FeedController {

    private static final String NUM_OF_UNREAD_NEWS = "num_of_unread_news_for ";
    private static final Logger LOG = LoggerFactory.getLogger(FeedController.class);
    private final Queue<DeferredResult<Integer>> responseBodyQueue = new ConcurrentLinkedQueue<>();
    @Autowired
    private CacheProvider<String, Integer> cache;

    public FeedController() {
        LOG.info("");
    }

    @RequestMapping("/feed")
    public String friendsActivity(Model model) {
        return "followingHistory";
    }

    @RequestMapping(value = "/num_of_unread", method = RequestMethod.GET)
    public
    @ResponseBody
    DeferredResult<Integer> getNumOfUnreadNews() {
        DeferredResult<Integer> result = new DeferredResult<>();
        this.responseBodyQueue.add(result);
        return result;
    }

    @Scheduled(fixedRate = 2000)
    public void processQueues() {
        for (DeferredResult<Integer> result : this.responseBodyQueue) {
            Integer numOfUnread = cache.get(NUM_OF_UNREAD_NEWS + SecurityUtil.currentUserId());
            result.setResult(numOfUnread);
            this.responseBodyQueue.remove(result);
        }
    }

}

