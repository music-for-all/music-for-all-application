package com.musicforall.services.follower;

import com.musicforall.common.dao.Dao;
import com.musicforall.model.Followers;
import com.musicforall.model.User;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
        if (userId.equals(following_userId)) {
            return;
        }
        Followers followers = dao.get(Followers.class, userId);
        if (followers == null) {
            followers = new Followers(userId);
        }
        followers.follow(following_userId);
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
                .createAlias("followingId", "integer");
        detachedCriteria.add(Restrictions.sqlRestriction(" following_id LIKE '%" + userId + "%' "));
        final List<Followers> followers = dao.getAllBy(detachedCriteria);
        final List<Integer> followersId = new ArrayList<>();
        for (final Followers followers1 : followers) {
            followersId.add(followers1.getFollowerId());
        }
        return followersId;
    }

    @Override
    public List<Integer> getFollowingId(Integer userId) {
        final Followers followers = dao.get(Followers.class, userId);
        return followers.getFollowingId();
    }

    @Override
    public List<User> getFollowers(Integer userId) {
        final List<Integer> followersId = getFollowersId(userId);
        if (followersId.isEmpty()) {
            return new ArrayList<>();
        }
        final Disjunction disjunction = Restrictions.disjunction();
        for (final Integer follower : followersId) {
            disjunction.add(Restrictions.eq("id", follower));
        }
        final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class)
                .add(disjunction);
        return dao.getAllBy(detachedCriteria);
    }

    @Override
    public List<User> getFollowing(Integer userId) {
        final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
        final List<Integer> followingId = getFollowingId(userId);
        if (followingId.isEmpty()) {
            return new ArrayList<>();
        }
        final Disjunction disjunction = Restrictions.disjunction();
        for (final Integer follower : followingId) {
            disjunction.add(Restrictions.eq("id", follower));
        }
        detachedCriteria.add(disjunction);
        return dao.getAllBy(detachedCriteria);
    }
}
