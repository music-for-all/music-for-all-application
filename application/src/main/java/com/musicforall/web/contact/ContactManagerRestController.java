package com.musicforall.web.contact;

import com.musicforall.common.Constants;
import com.musicforall.model.user.User;
import com.musicforall.services.follower.FollowerService;
import com.musicforall.services.user.UserService;
import com.musicforall.util.SecurityUtil;
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

/**
 * Created by Andrey on 7/31/16.
 */
@RestController
@RequestMapping("/contactManager")
public class ContactManagerRestController {

    private static final Logger LOG = LoggerFactory.getLogger(ContactManagerRestController.class);

    @Autowired
    private FollowerService followerService;

    @Autowired
    private UserService userService;

    public ContactManagerRestController() {
        LOG.debug("Contact manager RestController");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public ResponseEntity follow(@PathVariable(Constants.ID) Integer user_id) {
        followerService.follow(SecurityUtil.currentUser().getId(), user_id);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity unfollow(@PathVariable(Constants.ID) Integer user_id) {
        followerService.unfollow(SecurityUtil.currentUser().getId(), user_id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/followers", method = RequestMethod.GET)
    public Collection<User> getFollowers() {
        return userService.getUsersById(followerService.getFollowersId(SecurityUtil.currentUser().getId()));
    }

    @RequestMapping(value = "/following", method = RequestMethod.GET)
    public Collection<User> getFollowings() {
        return userService.getUsersById(followerService.getFollowingId(SecurityUtil.currentUser().getId()));
    }

    @RequestMapping(value = "/search={username}", method = RequestMethod.GET)
    public Collection<User> search(@PathVariable("username") String username) {
        return userService.getUsersByUsername(username);
    }
}
