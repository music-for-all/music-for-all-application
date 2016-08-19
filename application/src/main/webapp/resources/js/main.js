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
        $("#recommendations #" + id + " .num-likes").text(likeCount);
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
            updateLikeCount(track.id);
        });
    });
}
