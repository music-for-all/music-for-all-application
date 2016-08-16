"use strict";

/**
 * The Track object encapsulates operations with the Track RESTful service.
 * @author ENikolskiy on 6/26/2016.
 */
function Track() {

    var self = this;
    var baseUrl = dict.contextPath + "/tracks";

    /**
     * Posts the upload track data via Ajax.
     * @param formData the data to be posted
     */
    self.createJson = function (formData) {
        return $.when(
            $.ajax({
                url: "/files",
                type: "POST",
                data: formData,
                cache: false,
                processData: false,
                contentType: false
            }));
    };

    /**
     * Sends a request to delete a track with the specified id.
     * @param id the id of the track
     */
    self.remove = function (id) {
        return $.when(
            $.ajax({
                url: baseUrl + "/" + id,
                type: "DELETE"
            }));
    };

    /**
     * Creates a track with the specified name.
     * @param name of the track
     */
    self.create = function (name) {
        return $.when($.post(baseUrl, {"name": name}));
    };

    /**
     * Retrieves a track with the specified id.
     * @param id the id of the track
     */
    self.get = function (id) {
        return $.when($.get(baseUrl + "/" + id));
    };
}