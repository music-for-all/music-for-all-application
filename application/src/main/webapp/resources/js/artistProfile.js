"use strict";

$(document).ready(function() {

    var artist = new Artist();
    var name = $(".panel-title").text();
    artist.getInfo(name)
        .then(function (data) {
            var artist = data.artist;
            $(".panel-title").text(artist.name);
            $(".artist-name").text(artist.name);
            console.log(artist.image);
            console.log(artist.bio.summary);
        });
});
