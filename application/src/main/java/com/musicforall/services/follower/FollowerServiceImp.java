package com.musicforall.services.follower;

import com.musicforall.common.cache.CacheProvider;
import com.musicforall.common.dao.Dao;
import com.musicforall.model.Followers;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by andrey on 8/2/16.
 */
@Service("followerService")
@Transactional
public class FollowerServiceImp implements FollowerService {

    private static final String NUM_OF_UNREAD_NEWS = "num_of_unread_news_for ";

    @Autowired
    private Dao dao;

    @Autowired
    private CacheProvider<String, Integer> cache;

    @Override
    public void follow(Integer userId, Integer followingUserId) {
        Followers followers = dao.get(Followers.class, userId);
        if (followers == null) {
            followers = new Followers(userId);
        }
        if (!userId.equals(followingUserId)) {
            incrementNumOfUnread(followingUserId);
            followers.follow(followingUserId);
        }
        dao.save(followers);
    }

    @Override
    public void unfollow(Integer userId, Integer followingUserId) {
        final Followers followers = dao.get(Followers.class, userId);
        followers.unfollow(followingUserId);
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

    private void incrementNumOfUnread(Integer userId) {
        Integer numOfUnread = cache.get(NUM_OF_UNREAD_NEWS + userId);
        if (numOfUnread != null) {
            numOfUnread++;
        } else {
            numOfUnread = 1;
        }
        cache.put(NUM_OF_UNREAD_NEWS + userId, numOfUnread);
    }
}
