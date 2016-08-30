"use strict";

/**
 * The Playlist object encapsulates operations with the Playlist RESTful service.
 * @author ENikolskiy on 6/24/2016.
 */
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
}