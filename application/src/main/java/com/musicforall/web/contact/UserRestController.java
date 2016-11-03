package com.musicforall.web.contact;

import com.musicforall.common.Constants;
import com.musicforall.model.SearchUserRequest;
import com.musicforall.model.user.User;
import com.musicforall.model.user.UserData;
import com.musicforall.services.follower.FollowerService;
import com.musicforall.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static com.musicforall.util.SecurityUtil.currentUser;

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
    public ResponseEntity follow(@PathVariable(Constants.ID) Integer userId) {
        followerService.follow(currentUser().getId(), userId);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity unfollow(@PathVariable(Constants.ID) Integer userId) {
        followerService.unfollow(currentUser().getId(), userId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/followers", method = RequestMethod.GET)
    public Collection<UserData> getFollowers() {
        return userService.getAllUserDataByUserId(followerService.getFollowersId(currentUser().getId()));
    }

    @RequestMapping(value = "/following", method = RequestMethod.GET)
    public Collection<UserData> getFollowings() {
        return userService.getAllUserDataByUserId(followerService.getFollowingId(currentUser().getId()));
    }

    @RequestMapping(value = "/search={username}", method = RequestMethod.GET)
    public Collection<UserData> search(@PathVariable("username") String username) {
        return userService.getAllUserDataLike(new SearchUserRequest(username));
    }

    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public ResponseEntity me() {
        final User currentUser = currentUser();
        if (currentUser == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }
}
