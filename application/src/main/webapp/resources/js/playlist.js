"use strict";

function Playlist() {

    var self = this;
    var baseUrl = dict.contextPath + "/playlists";

    self.remove = function (id) {
        return $.when(
            $.ajax({
                url: baseUrl + "/" + id,
                type: "DELETE"
            }));
    };

    self.create = function (name) {
        return $.when($.post(baseUrl, {"name": name}));
    };

    self.get = function (id) {
        return $.when($.get(baseUrl + "/" + id));
    };

    self.all = function () {
        return $.when($.get(baseUrl));
    };

    self.addTrack = function (playlistId, trackId) {
        return $.when($.ajax({
            type: "POST",
            url: baseUrl + "/" + playlistId + "/add/" + trackId
        }));
    };


    self.removeTrack = function (playlistId, trackId) {
        return $.when($.ajax({
            type: "DELETE",
            url: baseUrl + "/" + playlistId + "/remove/" + trackId,
        }));
    };
}