package com.musicforall.services.follower;

import com.musicforall.common.dao.Dao;
import com.musicforall.model.Followers;
import com.musicforall.model.User;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by andrey on 8/2/16.
 */
@Service("followerService")
@Transactional
public class FollowerServiceImp implements FollowerService {

    @Autowired
    private Dao dao;

    @Override
    public void follow(Integer userId, Integer following_userId) {
        dao.save(new Followers(userId, following_userId));
    }

    @Override
    public void unfollow(Integer userId, Integer following_userId) {
        final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Followers.class)
                .add(Property.forName("following_id").eq(following_userId))
                .add(Property.forName("follower_id").eq(userId));
        final List<Followers> followers = dao.getAllBy(detachedCriteria);
        if (!followers.isEmpty()) {
            dao.delete(followers.get(0));
        }
    }

    @Override
    public Set<Followers> getFollowingId(Integer userId) {
        final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Followers.class)
                .add(Property.forName("follower_id").eq(userId));
        final List<Followers> followers = dao.getAllBy(detachedCriteria);
        return new HashSet<>(followers);
    }

    @Override
    public Set<Followers> getFollowerId(Integer userId) {
        final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Followers.class)
                .add(Property.forName("following_id").eq(userId));
        final List<Followers> followers = dao.getAllBy(detachedCriteria);
        return new HashSet<>(followers);
    }

    @Override
    public Set<User> getFollower(Integer userId) {
        final Set<User> users = new HashSet<>();
        final Set<Followers> followersId = getFollowerId(userId);
        for (Followers followers : followersId) {
            users.add(dao.get(User.class, followers.getFollower_id()));
        }
        return users;
    }

    @Override
    public Set<User> getFollowing(Integer userId) {
        final Set<User> users = new HashSet<>();
        final Set<Followers> followersId = getFollowingId(userId);
        for (Followers followers : followersId) {
            users.add(dao.get(User.class, followers.getFollowing_id()));
        }
        return users;
    }
}
