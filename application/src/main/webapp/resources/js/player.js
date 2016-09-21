"use strict";


jQuery(document).ready(function () {

    var $trackHtml;

    var player = new ChunksPlayer();

    function playNewTrack(trackId) {
        var check = false;

        if (player.isPlaying()) {
            player.pause();
        }
        if (!player.isCurrentTrack(trackId)) {
            track.get(trackId).then(function (tr) {
                player.play(tr);
                writeToFoot(tr);
            });
            check = true;
        }
        return check;
    }

    function writeToFoot(tr) {
        $("#nameInFoot").html(tr.name);
        $("#artistInFoot").html(tr.artist);
    }
    
    /* Make the play button to invoke play on the corresponding player. */
    $("#tracks").on("click", ".play-track-button", function () {
        $trackHtml = $(this).closest("tr");
        if (!playNewTrack($trackHtml[0].id)) player.resume();
    });

    $('#next').bind('click', function() {
        if ($trackHtml.next()[0].id) {
            $trackHtml = $trackHtml.next();
            if (!playNewTrack($trackHtml[0].id)) player.resume();
        }
    });

    $('#prev').bind('click', function() {
        if ($trackHtml.prev()[0].id) {
            $trackHtml = $trackHtml.prev();
            if (!playNewTrack($trackHtml[0].id)) player.resume();
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
