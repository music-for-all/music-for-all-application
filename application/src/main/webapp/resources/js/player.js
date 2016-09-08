"use strict";

function playCurrentTrack(trackid) {

    if (load_track != null) {
        player.pause();
    }

    player = new ChunksPlayer();
    track.get(trackid)
        .then(function (track) {
            if (track) {
                player.play(track);

                $("#nameInFoot").html(track.name);
                $("#artistInFoot").html(track.artist);
            }
        });
    load_track = trackid
}

jQuery(document).ready(function () {

    var $trackHtml;
    
    /* Make the play button to invoke play on the corresponding player. */
    $("#tracks").on("click", ".play-track-button", function () {
        $trackHtml = $(this).closest("tr");
        var curr = $trackHtml[0].id;

        if (load_track === curr) {
            player.resume();
        }
        else {
            playCurrentTrack(curr);
        }
    });

    $('#next').bind('click', function() {
        if ($trackHtml.next()[0].id) {
            $trackHtml = $trackHtml.next();
            playCurrentTrack($trackHtml[0].id);
        }
    });

    $('#prev').bind('click', function() {
        if ($trackHtml.prev()[0].id) {
            $trackHtml = $trackHtml.prev();
            playCurrentTrack($trackHtml[0].id);
        }
    });

    /* Make the pause button to invoke pause on the corresponding player. */
    $("#tracks").on("click", ".pause-track-button", function () {
        if ($(this).closest("tr")[0].id == load_track){
            player.pause();
        }
    });
    
});
