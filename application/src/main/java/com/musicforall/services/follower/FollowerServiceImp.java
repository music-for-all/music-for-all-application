package com.musicforall.services.follower;

import com.musicforall.common.dao.Dao;
import com.musicforall.model.Followers;
import org.hibernate.SessionFactory;
import com.musicforall.services.notification.NotificationService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.musicforall.notifications.Notification.Type.FOLLOWER;

/**
 * Created by andrey on 8/2/16.
 */
@Service("followerService")
@Transactional
public class FollowerServiceImp implements FollowerService {

    @Autowired
    private Dao dao;

    @Autowired
    public void setDao(@Autowired @Qualifier("main_session") SessionFactory sessionFactory) {
        dao.setSessionFactory(sessionFactory);
    }
    private NotificationService notificationService;

    @Override
    public void follow(Integer userId, Integer followingUserId) {
        Followers followers = dao.get(Followers.class, userId);
        if (followers == null) {
            followers = new Followers(userId);
        }
        if (!userId.equals(followingUserId)) {
            notificationService.fire(followingUserId, FOLLOWER);
            followers.follow(followingUserId);
        }
        dao.save(followers);
    }

    @Override
    public void unfollow(Integer userId, Integer followingUserId) {
        final Followers followers = dao.get(Followers.class, userId);
        notificationService.fire(followingUserId, FOLLOWER);
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


}
