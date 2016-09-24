"use strict";


jQuery(document).ready(function () {

    var $trackHtml;

    var elemCircle = $("<td class='circle'></td>");

    var player = new ChunksPlayer();
    var track = new Track();

    function playTrack(trackId) {

        if (player.getTrackId() == trackId) {
            player.resume();
        }
        else {
            elemCircle.remove();
            $trackHtml.children().last().after().append(elemCircle);
            track.get(trackId).then(function (tr) {
                player.play(tr);
                writeToFoot(tr);
            });
        }
        elemCircle.removeClass('pause').addClass('play');
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


    $('#nextFooterBtn').bind('click', function() {
        if ($trackHtml.next()[0].id) {
            $trackHtml = $trackHtml.next();
            playTrack($trackHtml[0].id);
        }
    });

    $('#prevFooterBtn').bind('click', function() {
        if ($trackHtml.prev()[0].id) {
            $trackHtml = $trackHtml.prev();
            playTrack($trackHtml[0].id);
        }
    });

    $('#playFooterBtn').bind('click', function() {
        player.resume();
        elemCircle.removeClass('pause').addClass('play');
    });

    $('#pauseFooterBtn').bind('click', function() {
        player.pause();
        elemCircle.addClass('pause');
    });

    /* Make the pause button to invoke pause on the corresponding player. */
    $("#tracks").on("click", ".pause-track-button", function () {
        if (player.getTrackId() == $(this).closest("tr")[0].id) {
            elemCircle.addClass('pause');
            player.pause();
        }
    });
});
