"use strict";

function History() {

    var self = this;
    var baseUrl = dict.contextPath + "/history";

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

    function requestTrackWithPlaylist(trackId, playlistId, url) {
        return $.when(
            $.ajax({
                url: baseUrl + url,
                type: "POST",
                data: {
                    trackId: trackId,
                    playlistId: playlistId
                }
            }));
    }

    self.trackLiked = function (trackId) {
        return request(trackId, "/track/liked");
    };

    self.trackAdded = function (trackId, playlistId) {
        return requestTrackWithPlaylist(trackId, playlistId, "/track/added");
    };

    self.trackDeleted = function (trackId, playlistId) {
        return requestTrackWithPlaylist(trackId, playlistId, "/track/deleted");
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
}