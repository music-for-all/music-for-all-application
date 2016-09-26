"use strict";

function Artist() {

    var self = this;

    self.getInfo = function (name) {

        var url = "http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&artist=" + name +
            "&api_key=2b35547bd5675d8ecb2b911ee9901f59&format=json";

        return $.when(
            $.ajax({
                type: "GET",
                url: url

            }).fail(function (xhr, status, errorThrown) {
                console.log("Error: " + errorThrown);
                console.log("Status: " + status);
                console.dir(xhr);
            }));
    }
}