package com.musicforall.web;

import com.musicforall.model.User;
import com.musicforall.services.user.UserService;
import com.musicforall.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Andrey on 7/31/16.
 */
@RestController
@RequestMapping("/contactManager")
public class ContactManagerRestController {

    private static final Logger LOG = LoggerFactory.getLogger(ContactManagerRestController.class);

    @Autowired
    private UserService userService;

    public ContactManagerRestController() {
        LOG.debug("Contact manager RestController");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public ResponseEntity follow(@PathVariable("id") Integer user_id) {
        userService.follow(SecurityUtil.currentUser().getId(), user_id);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity unfollow(@PathVariable("id") Integer user_id) {
        userService.unfollow(SecurityUtil.currentUser().getId(), user_id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/followers", method = RequestMethod.GET)
    public Collection<User> getFollowers() {
        return userService.getFollowers(SecurityUtil.currentUser().getId());
    }

    @RequestMapping(value = "/following", method = RequestMethod.GET)
    public Collection<User> getFollowings() {
        return userService.getFollowing(SecurityUtil.currentUser().getId());
    }

    @RequestMapping(value = "/search={username}", method = RequestMethod.GET)
    public Collection<User> search(@PathVariable("username") String username) {
        final HashSet<User> users = new HashSet<>();
        users.add(userService.getByUsername(username));
        return users;
    }
}
