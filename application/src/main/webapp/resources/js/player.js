"use strict";


jQuery(document).ready(function () {

    var $trackHtml;

    var player = new ChunksPlayer();

    function playTrack(trackId) {
        if (!player.isCurrentTrack(trackId)) {
            track.get(trackId).then(function (tr) {
                player.play(tr);
                writeToFoot(tr);
            });
        }
        else {
            player.resume();
        }
    }

    function writeToFoot(tr) {
        $("#nameInFoot").html(tr.name);
        $("#artistInFoot").html(tr.artist);
    }
    
    /* Make the play button to invoke play on the corresponding player. */
    $("#tracks").on("click", ".play-track-button", function () {
        $trackHtml = $(this).closest("tr");
        playTrack($trackHtml[0].id);
    });

    $('#next').bind('click', function() {
        if ($trackHtml.next()[0].id) {
            $trackHtml = $trackHtml.next();
            playTrack($trackHtml[0].id);
        }
    });

    $('#prev').bind('click', function() {
        if ($trackHtml.prev()[0].id) {
            $trackHtml = $trackHtml.prev();
            playTrack($trackHtml[0].id);
        }
    });

    $('#playFooterBtn').bind('click', function() {
        player.resume();
    });

    $('#pauseFooterBtn').bind('click', function() {
        player.pause();
    });

    /* Make the pause button to invoke pause on the corresponding player. */
    $("#tracks").on("click", ".pause-track-button", function () {
        if (player.isCurrentTrack($(this).closest("tr")[0].id)){
            player.pause();
        }
    });
});
