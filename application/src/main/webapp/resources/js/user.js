"use strict";

function User() {

    var self = this;
    var baseUrl = dict.contextPath + "/user";

    self.unfollow = function (id) {
        return $.when(
            $.ajax({
                url: baseUrl + "/" + id,
                type: "DELETE"
            }));
    };

    self.follow = function (id) {
        return $.when($.post(baseUrl + "/" + id));
    };

    self.save = function (user) {
        return $.when(
            $.ajax({
                url: baseUrl,
                type: "POST",
                data: JSON.stringify(user),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                }
            }));
    };

    self.getFollowers = function () {
        return $.when($.get(baseUrl + "/followers"));
    };

    self.getFollowing = function () {
        return $.when($.get(baseUrl + "/following"));
    };

    self.search = function (username) {
        return $.when($.get(baseUrl + "/search=" + username));
    };

    self.me = function () {
        return $.when($.get(baseUrl + "/me"));
    };
}