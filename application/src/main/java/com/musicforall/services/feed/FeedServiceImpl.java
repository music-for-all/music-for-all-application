package com.musicforall.services.feed;

import com.musicforall.history.model.History;
import com.musicforall.history.service.history.HistoryService;
import com.musicforall.model.User;
import com.musicforall.services.follower.FollowerService;
import com.musicforall.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author IliaNik on 02.09.2016.
 */

@Service("feedService")
@Transactional
public class FeedServiceImpl implements FeedService {

    @Autowired
    private FollowerService followerService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private UserService userService;


    @Override
    public Map<User, List<History>> getGroupedFollowingHistories(Integer userId) {
        final Collection<Integer> usersIds = followerService.getFollowingId(userId);

        List<User> users = userService.getUsersById(usersIds);

        return historyService.getUsersHistories(usersIds)
                .stream()
                .collect(Collectors.groupingBy(p -> users
                                .stream()
                                .filter(u -> u.getId().equals(p.getUserId()))
                                .findFirst()
                                .get(),
                        LinkedHashMap::new,
                        Collectors.mapping(p -> p, Collectors.toList())));
    }
}
