"use strict";

function Playlist() {

    var self = this;
    var baseUrl = dict.contextPath + "/playlists";
    var history = new History();

    self.remove = function (id) {
        history.playlistDeleted(id);
        return $.when(
            $.ajax({
                url: baseUrl + "/" + id,
                type: "DELETE"
            }));
    };

    self.create = function (name) {
        var promise = $.when($.post(baseUrl, {"name": name}));
        promise.then(function (playlist) {
            history.playlistAdded(playlist.id);
        });
        return promise;
    };

    self.get = function (id) {
        return $.when($.get(baseUrl + "/" + id));
    };

    self.all = function () {
        return $.when($.get(baseUrl));
    };

    self.addTracks = function (playlistId, tracksIds) {
        tracksIds.forEach(function (trackId) {
            history.trackAdded(trackId);
        });
        return $.when($.post(baseUrl + "/" + playlistId + "/add/tracks",
            {
                tracksIds: tracksIds
            }));
    };

    self.removeTrack = function (playlistId, trackId) {
        history.trackDeleted(trackId);
        return $.when($.ajax({
            type: "DELETE",
            url: baseUrl + "/" + playlistId + "/remove/" + trackId
        }));
    };
}