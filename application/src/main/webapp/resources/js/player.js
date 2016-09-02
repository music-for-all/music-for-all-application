"use strict";

jQuery(document).ready(function () {

    /* Make the play button to invoke play on the corresponding player. */
    $("#tracks").on("click", ".play-track-button", function () {

        $(this).closest("tr").find("td audio")[0].play();
    });

    /* Make the pause button to invoke pause on the corresponding player. */
    $("#tracks").on("click", ".pause-track-button", function () {

        $(this).closest("tr").find("td audio")[0].pause();
    });

    /* When a track is chosen to play, pause all the other tracks. */
    document.addEventListener("play", function(e) {

        $("audio").each(function(i, player) {
            if (player !== e.target) {
                player.pause();
            }
        });
    }, true);
});
