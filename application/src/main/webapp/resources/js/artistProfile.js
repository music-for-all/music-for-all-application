"use strict";

$(document).ready(function() {

    var artist = new Artist();
    var name = $(".panel-title").text();

    artist.getInfo(name)
        .then(function (data) {
            var artist = data.artist;
            $(".artist-name").text(artist.name);
            console.log(artist.image);
            $("#artist-picture").attr("src", artist.image);
            console.log(artist.bio.summary);
            $("#artist-bio").text(artist.bio.summary);
        });
});
