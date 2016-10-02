package com.musicforall.web.contact;

import com.musicforall.common.Constants;
import com.musicforall.model.user.User;
import com.musicforall.services.follower.FollowerService;
import com.musicforall.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static com.musicforall.util.SecurityUtil.currentUser;
import static com.musicforall.util.SecurityUtil.currentUserId;

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

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public ResponseEntity follow(@PathVariable(Constants.ID) Integer user_id) {
        followerService.follow(currentUser().getId(), user_id);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity save(@RequestBody User user) {
        userService.get(user.getId());
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity unfollow(@PathVariable(Constants.ID) Integer user_id) {
        followerService.unfollow(currentUser().getId(), user_id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/followers", method = RequestMethod.GET)
    public Collection<User> getFollowers() {
        return userService.getUsersById(followerService.getFollowersId(currentUser().getId()));
    }

    @RequestMapping(value = "/following", method = RequestMethod.GET)
    public Collection<User> getFollowings() {
        return userService.getUsersById(followerService.getFollowingId(currentUser().getId()));
    }

    @RequestMapping(value = "/search={username}", method = RequestMethod.GET)
    public Collection<User> search(@PathVariable("username") String username) {
        return userService.getUsersByUsername(username);
    }

    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public ResponseEntity me() {
        final Integer currentUserId = currentUserId();
        if (currentUserId == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        final User meWithOptions = userService.getWithOptionsById(currentUserId);
        return new ResponseEntity<>(meWithOptions, HttpStatus.OK);
    }
}
