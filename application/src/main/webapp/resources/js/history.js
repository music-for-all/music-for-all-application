"use strict";

function History() {

    var self = this;
    var baseUrl = dict.contextPath + "/history";

    self.trackLiked = function (trackId) {
        return $.when($.post(baseUrl + "/track/liked"), {"trackId": trackId});
    };

    self.trackAdded = function (trackId, playlistId) {
        return $.when($.post(baseUrl + "/track/added", {"trackId": trackId, "playlistId": playlistId}));
    };

    self.trackDeleted = function (trackId, playlistId) {
        return $.when($.post(baseUrl + "/track/deleted", {"trackId": trackId, "playlistId": playlistId}));
    };

    self.trackListened = function (trackId) {
        return $.when($.post(baseUrl + "/track/listened"), {"trackId": trackId});
    };

    self.playlistAdded = function (playlistId) {
        return $.when($.post(baseUrl + "/playlist/added"), {"playlistId": playlistId});
    };

    self.playlistDeleted = function (playlistId) {
        return $.when($.post(baseUrl + "/playlist/deleted"), {"playlistId": playlistId});
    };
}