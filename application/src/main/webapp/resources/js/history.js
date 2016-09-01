"use strict";

function History() {

    var self = this;
    var baseUrl = dict.contextPath + "/history";

    self.trackLiked = function (trackId) {
        return request(trackId, "/track/liked");
    };

    self.trackAdded = function (trackId) {
        return request(trackId, "/track/added");
    };

    self.trackDeleted = function (trackId) {
        return request(trackId, "/track/deleted");
    };

    self.trackListened = function (trackId) {
        return request(trackId, "/track/listened");
    };

    self.playlistAdded = function (playlistId) {
        return request(playlistId, "/playlist/added");
    };

    self.playlistDeleted = function (playlistId) {
        return request(playlistId, "/playlist/deleted");
    };

    function request(id, url) {
        return $.when(
            $.ajax({
                url: baseUrl + url,
                type: "POST",
                data: {
                    id: id
                }
            }));
    }
}