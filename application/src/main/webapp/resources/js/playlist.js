"use strict";

/**
 * The Playlist object encapsulates operations with the Playlist RESTful service.
 * @param contextPath the context path of the application
 * @author ENikolskiy on 6/24/2016.
 */
function Playlist(contextPath) {

    var self = this;
    var baseUrl = contextPath + "/playlists";

    /**
     * Deletes a playlist with the specified id.
     * @param id the id of the playlist to be deleted
     */
    self.remove = function (id) {
        return $.when(
            $.ajax({
                url: baseUrl + "/" + id,
                type: "DELETE"
            }));
    };

    /**
     * Creates a new playlist with the given name.
     * @param name the name of the new playlist
     */
    self.create = function (name) {
        return $.when($.post(baseUrl, {"name": name}));
    };

    /**
     * Retrieves a playlist with the given id.
     * @param id the id of the playlist
     */
    self.get = function (id) {
        return $.when($.get(baseUrl + "/" + id));
    };

    /**
     * Retrieves all playlists (of the current user).
     */
    self.all = function () {
        return $.when($.get(baseUrl));
    };
}