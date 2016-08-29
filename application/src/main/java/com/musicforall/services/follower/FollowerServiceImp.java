package com.musicforall.services.follower;

import com.musicforall.common.dao.Dao;
import com.musicforall.history.model.History;
import com.musicforall.history.service.history.HistoryService;
import com.musicforall.model.Followers;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by andrey on 8/2/16.
 */
@Service("followerService")
@Transactional
public class FollowerServiceImp implements FollowerService {

    @Autowired
    private Dao dao;

    @Autowired
    private HistoryService historyService;

    @Override
    public void follow(Integer userId, Integer following_userId) {
        Followers followers = dao.get(Followers.class, userId);
        if (followers == null) {
            followers = new Followers(userId);
        }
        if (!userId.equals(following_userId)) {
            followers.follow(following_userId);
        }
        dao.save(followers);
    }

    @Override
    public void unfollow(Integer userId, Integer following_userId) {
        final Followers followers = dao.get(Followers.class, userId);
        followers.unfollow(following_userId);
        dao.save(followers);
    }

    @Override
    public List<Integer> getFollowersId(Integer userId) {
        final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Followers.class)
                .createAlias("followingId", "userId");
        detachedCriteria.add(Restrictions.sqlRestriction("following_id = '" + userId + "'"));
        final List<Followers> followers = dao.getAllBy(detachedCriteria);
        return followers.stream()
                .map(Followers::getFollowerId).collect(Collectors.toList());
    }

    @Override
    public Collection<Integer> getFollowingId(Integer userId) {
        Followers followers = dao.get(Followers.class, userId);
        if (followers == null) {
            followers = new Followers(userId);
        }
        return followers.getFollowingId();
    }

    @Override
    public LinkedHashMap<Integer, List<History>> getGroupedFollowingHistories(Integer userId) {
        final Collection<Integer> usersIds = getFollowingId(userId);

        return historyService.getUsersHistories(usersIds)
                .stream()
                .collect(Collectors.groupingBy(History::getUserId, LinkedHashMap::new,
                        Collectors.mapping(p -> p, Collectors.toList())));
    }
}
