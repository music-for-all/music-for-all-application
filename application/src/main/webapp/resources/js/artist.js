"use strict";

function Artist(name) {

    var self = this;

    self.getInfo = function() {

        var url = "http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&artist=" + name +
            "&api_key=2b35547bd5675d8ecb2b911ee9901f59&format=json";

        return $.when(
            $.ajax({
                type: "GET",
                url: url

            }).fail(function (xhr, status, errorThrown) {
                console.log("Error: " + errorThrown);
                console.log("Status: " + status);
            }));
    };

    self.getTopTracks = function() {

        var url = "/tracks/topTracksOf/" + name;

        return $.when($.ajax({
            type: "GET",
            url: url
        }));
    }

    self.getTopAlbums = function() {

        var url = "/tracks/topAlbumsOf/" + name;

        return $.when($.ajax({
            type: "GET",
            url: url
        }));
    }
}