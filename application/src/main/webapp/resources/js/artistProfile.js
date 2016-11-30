"use strict";

$(document).ready(function() {

    var artistName = $(".panel-title").text();
    var artist = new Artist(artistName);

    _.templateSettings.variable = "data";
    var trackTable = _.template(
        $("script.addTrackRowTemplate").html()
    );

    artist.getInfo()
        .then(function (data) {
            var artist = data.artist;

            $("#artist-name").text(artist.name);

            $("#artist-picture").attr("src", artist.image[3]["#text"]);

            $("#artist-bio").html(artist.bio.summary);
        });

    artist.getTopTracks()
        .then(function (response) {

            response.forEach(function (track) {
                $("#tracks").find("thead").after(
                    trackTable(track)
                );
            });
        });

    artist.getTopAlbums()
        .then(function (response) {

            response.forEach(function (album) {
                $("#albums").find("thead").after(
                    $("<tr>").text(album));
            });
        });
});
