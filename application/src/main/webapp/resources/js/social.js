"use strict";

function Social() {

    var self = this;
    var baseUrl = dict.contextPath + "/user";

    self.getUserFollowers = function (id) {
        return $.when($.get(baseUrl + "/followers/"+id));
    };

    self.getUserFollowing = function (id) {
        return $.when($.get(baseUrl + "/following/"+id));
    };

    self.getUser = function (id) {
        return $.when($.get(baseUrl + "/" + id));
    };

    self.getUserPlaylists = function (id) {
        return $.when($.get(baseUrl + "/playlists/"+id));
    }

}
