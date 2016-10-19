"use strict";

$(document).ready(function() {

    var artist = new Artist();
    var name = $(".panel-title").text();

    artist.getInfo(name)
        .then(function (data) {
            var artist = data.artist;
            console.log(artist);
            $("#artist-name").text(artist.name);

            $("#artist-picture").attr("src", artist.image[3]["#text"]);

            $("#artist-bio").html(artist.bio.summary);
        });
});
