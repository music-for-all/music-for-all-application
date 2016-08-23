"use strict";

function Track() {

    var self = this;
    var baseUrl = dict.contextPath + "/tracks";

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

    self.like = function (id) {
        return $.when(
            $.ajax({
                type: "POST",
                url: baseUrl + "/like/" + id

            }).fail(function (xhr, status, errorThrown) {
                var message = status + ": " + xhr.status + " " + errorThrown;
                console.log(message);

            }));
    };

    self.getLikeCount = function (id) {
        return $.when(
            $.ajax({
                type: "GET",
                url: baseUrl + "/like/" + id

            }).fail(function (xhr, status, errorThrown) {
                var message = status + ": " + xhr.status + " " + errorThrown;
                console.log(message);
            }));
    }

    self.getRecommendedTracks = function (id) {
        return $.when(
            $.ajax({
                type: "GET",
                url: baseUrl + "/recommended",
                dataType: "json"

            }).fail(function (xhr, status, errorThrown) {
                var message = status + ": " + xhr.status + " " + errorThrown;
                console.log(message);

            }));
    }
}