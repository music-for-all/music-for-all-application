package com.musicforall.web.profile;

import com.musicforall.dto.profile.ProfileData;
import com.musicforall.services.user.UserService;
import com.musicforall.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by Andrey on 9/26/16.
 */
@RestController
@RequestMapping("/profile")
public class ProfileRestController {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileRestController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/currentUser", method = RequestMethod.GET)
    public ResponseEntity getCurrentUser() {
        return new ResponseEntity<>(userService.getWithUserDataById(SecurityUtil.currentUserId()), HttpStatus.OK);
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public ResponseEntity update(@Valid ProfileData profileData, BindingResult bindingResult) {
        LOG.info(profileData.toString());

        if (bindingResult.hasErrors()) {
            LOG.info(bindingResult.getAllErrors().toString());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        userService.updateUserData(SecurityUtil.currentUserId(), profileData);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/update/password", method = RequestMethod.GET)
    public ResponseEntity updatePassword(@Valid ProfileData profileData, BindingResult bindingResult) {
        LOG.info(profileData.toString());

        if (bindingResult.hasErrors()) {
            LOG.info(bindingResult.getAllErrors().toString());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        userService.updateUser(SecurityUtil.currentUserId(), profileData);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
