"use strict";

function like(id) {

    var baseUrl = dict.contextPath + "/tracks";

    $.ajax({
        type: "POST",
        url: baseUrl + "/like/" + id

    }).fail(function (xhr, status, errorThrown) {
        var message = status + ": " + xhr.status + " " + errorThrown;
        console.log(message);

    }).done(function (result, status) {

        $("#" + id + " .like-button").css("opacity", "0.5");
        updateLikeCount(id);
    });
}

function updateLikeCount(id) {

    var baseUrl = dict.contextPath + "/tracks";

    $.ajax({
        type: "GET",
        url: baseUrl + "/like/" + id

    }).fail(function (xhr, status, errorThrown) {
        var message = status + ": " + xhr.status + " " + errorThrown;
        console.log(message);

    }).done(function(likeCount) {
        $("#tracks #" + id + " .num-likes").text(likeCount);
    });
}

/**
 * Retrieves tracks recommended for the current user.
 */
function displayRecommendedTracks() {

    var baseUrl = dict.contextPath + "/tracks";
    $.ajax({
        type: "GET",
        url: baseUrl + "/recommended",
        dataType: "json"

    }).fail(function (xhr, status, errorThrown) {
        var message = status + ": " + xhr.status + " " + errorThrown;
        console.log(message);

    }).done(function (tracks) {
        $.each(tracks, function (i, track) {
            addRecommendedTrack(track);
        });
    });
}

jQuery(document).ready(function () {

    /* Handle the Like button (Ajax). */
    $("#tracks").on("click", ".like-button", function () {

        /* The id of a track is stored in the containing <tr> element. */
        var id = $(this).closest("tr").attr("id");
        like(id);
    });

    /*
     * When a recommended track is clicked, add it to the playlist,
     */
    $("#recommendations").on("click", "a", function (e) {
        e.preventDefault();

        var li = $(this).closest("li");
        var trackId = li.attr("id");
        var playlistId = $("#playlists li.active").attr("id");

        playlist.addTrack(playlistId, trackId)
            .then(function() {
                /* Remove the track from the recommended section, and update the current playlist. */
                li.remove();
                $("#playlists li.active a").trigger("click");
            });
    });

    displayRecommendedTracks();
});
