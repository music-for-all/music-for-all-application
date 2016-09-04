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
    load_track = trackid;
    
}

jQuery(document).ready(function () {
    
    /* Make the play button to invoke play on the corresponding player. */
    $("#tracks").on("click", ".play-track-button", function (e) {

        var curr = $(this).closest("tr").find("td div")[0].id;

        if (load_track === curr) {
            player.resume();
        }
        else {
            playCurrentTrack(curr);
        }
    });
    
    /* Make the pause button to invoke pause on the corresponding player. */
    $("#tracks").on("click", ".pause-track-button", function () {
        player.pause();
    });
});
