package com.musicforall.web.social;

import com.musicforall.common.Constants;
import com.musicforall.model.Playlist;
import com.musicforall.model.SearchUserRequest;
import com.musicforall.model.user.User;
import com.musicforall.services.follower.FollowerService;
import com.musicforall.services.playlist.PlaylistService;
import com.musicforall.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/social")
public class SocialRestController {

    @Autowired
    private FollowerService followerService;

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/followers/{id}", method = RequestMethod.GET)
    public List<User> getUserFollowers(@PathVariable(Constants.ID) Integer user_id) {
        List<Integer> followersId = followerService.getFollowersId(user_id);
        List<User> followers = userService.getUsersById(followersId);

        return followers;
    }

    @RequestMapping(value = "/following/{id}", method = RequestMethod.GET)
    public List<User> getUserFollowing(@PathVariable(Constants.ID) Integer user_id) {
        Collection<Integer> followingId = followerService.getFollowingId(user_id);
        List<User> following = userService.getUsersById(followingId);

        return following;
    }

    @RequestMapping(value = "/playlists/{id}", method = RequestMethod.GET)
    public Set<Playlist> getUserPlaylists(@PathVariable(Constants.ID) Integer user_id) {
        Set<Playlist> playlists = playlistService.getAllUserPlaylists(user_id);

        return playlists;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User getUser(@PathVariable(Constants.ID) Integer user_id) {
        return userService.get(user_id);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity getUsersLike(@RequestParam("searchTerm") final String searchTerm) {

        final List<User> users = userService.getAllLike(new SearchUserRequest(searchTerm));

        if (users.isEmpty()) {
            SearchUserRequest searchRequest = new SearchUserRequest();
            searchRequest.setFirstName(searchTerm);

            final List<User> usersByFirstName = userService.getAllLike(searchRequest);
            final List<String> usersUsernameByEmail = usersByFirstName.stream()
                    .map(u -> u.getUserData().getUsername()).collect(Collectors.toList());

            return new ResponseEntity<>(usersUsernameByEmail, HttpStatus.OK);
        } else {
            final List<String> usersUsername = users.stream().map(u -> u.getUserData().getUsername())
                    .collect(Collectors.toList());

            return new ResponseEntity<>(usersUsername, HttpStatus.OK);
        }
    }

}
