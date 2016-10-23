package com.musicforall.web.contact;

import com.musicforall.common.Constants;
import com.musicforall.model.SearchUserRequest;
import com.musicforall.model.user.User;
import com.musicforall.model.user.UserAchievement;
import com.musicforall.services.achievements.UserAchievementsService;
import com.musicforall.services.follower.FollowerService;
import com.musicforall.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static com.musicforall.util.SecurityUtil.currentUser;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by Andrey on 7/31/16.
 */
@RestController
@RequestMapping("/user")
public class UserRestController {

    private static final Logger LOG = LoggerFactory.getLogger(UserRestController.class);

    @Autowired
    private FollowerService followerService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserAchievementsService userAchievementsService;

    @RequestMapping(value = "/{id}", method = POST)
    public ResponseEntity follow(@PathVariable(Constants.ID) Integer user_id) {
        followerService.follow(currentUser().getId(), user_id);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/{id}", method = DELETE)
    public ResponseEntity unfollow(@PathVariable(Constants.ID) Integer user_id) {
        followerService.unfollow(currentUser().getId(), user_id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/followers", method = GET)
    public Collection<User> getFollowers() {
        return userService.getUsersById(followerService.getFollowersId(currentUser().getId()));
    }

    @RequestMapping(value = "/following", method = GET)
    public Collection<User> getFollowings() {
        return userService.getUsersById(followerService.getFollowingId(currentUser().getId()));
    }

    @RequestMapping(value = "/search={username}", method = GET)
    public Collection<User> search(@PathVariable("username") String username) {
        return userService.getAllLike(new SearchUserRequest(username));
    }

    @RequestMapping(value = "/me", method = GET)
    public ResponseEntity me() {
        final User currentUser = currentUser();
        if (currentUser == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/achievements", method = GET)
    public ResponseEntity achievements(@PathVariable(Constants.ID) Integer userId) {
        final Collection<UserAchievement> userAchievements = userAchievementsService.getAllByUserId(userId);
        return new ResponseEntity<>(userAchievements, HttpStatus.OK);
    }
}
