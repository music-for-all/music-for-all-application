package com.musicforall.web.user;

import com.musicforall.common.Constants;
import com.musicforall.model.Playlist;
import com.musicforall.model.user.User;
import com.musicforall.services.follower.FollowerService;
import com.musicforall.services.playlist.PlaylistService;
import com.musicforall.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/social")
public class UserRestController {

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

}
