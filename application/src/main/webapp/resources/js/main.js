"use strict";
function updateLikeCount(id) {
    $.ajax({
        type: "GET",
        url: "/tracks/like/" + id

    }).fail(function (xhr, status, errorThrown) {
        var message = status + ": " + xhr.status + " " + errorThrown;
        console.log(message);

    }).done(function(likeCount) {
        $("#" + id + " .num-likes").text(likeCount);
    });
}

function like(id) {

    console.log("Like: " + id);

    $.ajax({
        type: "POST",
        url: "/tracks/like/" + id,

    }).fail(function (xhr, status, errorThrown) {
        var message = status + ": " + xhr.status + " " + errorThrown;
        console.log(message);

    }).done(function (result, status) {

        $("#" + id + " .like-button").css("opacity", "0.5");
        updateLikeCount(id);

    });
}


jQuery(document).ready(function () {

    /* Handle the Like button (Ajax). */
    $("#tracks").on("click", ".like-button", function () {

        /* The id of a track is stored in the containing <tr> element. */
        var id = $(this).closest("tr").attr("id");
        like(id);
    });
});